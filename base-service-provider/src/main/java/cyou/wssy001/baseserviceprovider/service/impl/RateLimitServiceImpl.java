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
        LocalDateTime now = LocalDateTime.now(ZoneId.of("GMT+8"));
        LocalDateTime time = ENDPOINT_RESET_TIME.get(key);
        return time == null || time.isBefore(now);
    }

    @Override
    public boolean hasRemain(String user, String eventKey, PlatformEnum platform) {
        return CAFFEINE.getIfPresent(user + eventKey + platform.getCode()) == null;
    }

    @Override
    public void updateRemoteEndpointLimit(String remoteEndpoint, int resetTime, PlatformEnum platform) {
        LocalDateTime time = LocalDateTime.now(ZoneId.of("GMT+8"))
                .plusSeconds(resetTime);
        ENDPOINT_RESET_TIME.compute(remoteEndpoint + platform.getCode(), (k, v) -> time);
    }

    @Override
    public void updateUserLimit(String user, String eventKey, PlatformEnum platform) {
        CAFFEINE.put(user + eventKey + platform.getCode(), 1);
    }
}
