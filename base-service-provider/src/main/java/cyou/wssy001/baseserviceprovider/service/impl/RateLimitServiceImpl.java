package cyou.wssy001.baseserviceprovider.service.impl;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Expiry;
import cyou.wssy001.common.enums.EventEnum;
import cyou.wssy001.common.enums.PlatformEnum;
import cyou.wssy001.common.service.RateLimitService;
import org.checkerframework.checker.index.qual.NonNegative;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;

/**
 * @Description: API限速实现类
 * @Author: Tyler
 * @Date: 2023/4/28 9:10
 * @Version: 1.0
 */
@Service
public class RateLimitServiceImpl implements RateLimitService {
    private static final ConcurrentHashMap<String, LocalDateTime> ENDPOINT_RESET_TIME = new ConcurrentHashMap<>();
    //用户维度
    private static final Cache<String, Integer> USER_CAFFEINE = Caffeine.newBuilder()
            .maximumSize(300)
            .expireAfter(getExpireAfterCreateExpiry(1, TimeUnit.SECONDS))
            .build();
    //接口维度
    private static final Cache<String, LongAdder> ENDPOINT_CAFFEINE = Caffeine.newBuilder()
            .maximumSize(10)
            .expireAfter(getExpireAfterCreateExpiry(1, TimeUnit.MINUTES))
            .build();


    @Override
    public boolean hasRemain(String user, String eventKey, PlatformEnum platform) {
        String key = String.format("%s-%s-%d", user, eventKey, platform.getCode());
        return USER_CAFFEINE.asMap()
                .putIfAbsent(key, 1) == null;
    }

    @Override
    public boolean hasRemain(String remoteEndpoint, PlatformEnum platform, EventEnum eventType) {
        int platformCode = platform.getCode();
        if (platform.equals(PlatformEnum.KOOK)) {
            String key = remoteEndpoint + platformCode;
            LocalDateTime time = ENDPOINT_RESET_TIME.get(key);
            if (time != null) {
                LocalDateTime now = LocalDateTime.now(ZoneId.of("GMT+8"));
                if (time.isAfter(now)) return false;
            }
        }

        int eventCode = eventType.equals(EventEnum.GROUP) ? 1 : 2;
        String key = String.format("%s-%d-%d", remoteEndpoint, platformCode, eventCode);
        LongAdder computedLongAdder = ENDPOINT_CAFFEINE.asMap()
                .compute(key, (k, v) -> initAndCalcLongAdder(platform, eventCode, v));
        return computedLongAdder.sum() > 0;
    }

    private static LongAdder initAndCalcLongAdder(PlatformEnum platform, int eventCode, LongAdder v) {
        if (v == null) {
            v = new LongAdder();

            switch (platform) {
                case KOOK -> v.add(120);
                case DODO -> {
                    if (eventCode == 1) {
                        v.add(120);
                    } else {
                        v.add(20);
                    }
                }
            }
        }

        v.decrement();
        return v;
    }

    @Override
    public void updateRemoteEndpointLimit(String remoteEndpoint, int resetTime, PlatformEnum platform) {
        LocalDateTime time = LocalDateTime.now(ZoneId.of("GMT+8"))
                .plusSeconds(resetTime);
        String key = remoteEndpoint + platform.getCode();
        ENDPOINT_RESET_TIME.compute(key, (k, v) -> time);
    }

    private static <T, V> Expiry<T, V> getExpireAfterCreateExpiry(int num, TimeUnit timeUnit) {
        return new Expiry<>() {
            @Override
            public long expireAfterCreate(T key, V value, long currentTime) {
                return timeUnit.toNanos(num);
            }

            @Override
            public long expireAfterUpdate(T key, V value, long currentTime, @NonNegative long currentDuration) {
                return currentDuration;
            }

            @Override
            public long expireAfterRead(T key, V value, long currentTime, @NonNegative long currentDuration) {
                return currentDuration;
            }
        };
    }
}
