package cyou.wssy001.baseserviceprovider.service.impl;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import cyou.wssy001.common.enums.PlatformEnum;
import cyou.wssy001.common.service.RateLimitService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @Description: API限速实现类
 * @Author: Tyler
 * @Date: 2023/4/28 9:10
 * @Version: 1.0
 */
@Service
public class RateLimitServiceImpl implements RateLimitService {
    private static final ConcurrentHashMap<String, LocalDateTime> ENDPOINT_RESET_TIME = new ConcurrentHashMap<>();
    private static final Cache<String, Integer> CAFFEINE = Caffeine.newBuilder()
            .maximumSize(300)
            .expireAfterWrite(1, TimeUnit.SECONDS)
            .build();


    @Override
    public boolean hasRemain(String remoteEndpoint, PlatformEnum platform) {
        String key = remoteEndpoint + platform.getCode();
        LocalDateTime time = ENDPOINT_RESET_TIME.get(key);
        if (time == null) return true;

        LocalDateTime now = LocalDateTime.now(ZoneId.of("GMT+8"));
        return time.isBefore(now);
    }

    @Override
    public boolean hasRemain(String user, String eventKey, PlatformEnum platform) {
        String key = user + eventKey + platform.getCode();
        return CAFFEINE.getIfPresent(key) == null;
    }

    @Override
    public void updateRemoteEndpointLimit(String remoteEndpoint, int resetTime, PlatformEnum platform) {
        LocalDateTime time = LocalDateTime.now(ZoneId.of("GMT+8"))
                .plusSeconds(resetTime);
        String key = remoteEndpoint + platform.getCode();
        ENDPOINT_RESET_TIME.compute(key, (k, v) -> time);
    }

    @Override
    public void updateUserLimit(String user, String eventKey, PlatformEnum platform) {
        String key = user + eventKey + platform.getCode();
        CAFFEINE.put(key, 1);
    }
}
