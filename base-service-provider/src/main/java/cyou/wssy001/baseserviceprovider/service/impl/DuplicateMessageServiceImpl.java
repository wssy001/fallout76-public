package cyou.wssy001.baseserviceprovider.service.impl;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import cyou.wssy001.common.enums.PlatformEnum;
import cyou.wssy001.common.service.DuplicateMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @Description: 重复消息校验实现类
 * @Author: Tyler
 * @Date: 2023/5/4 15:20
 * @Version: 1.0
 */
@Slf4j
@Service
public class DuplicateMessageServiceImpl implements DuplicateMessageService {

    private static final Cache<String, Integer> CAFFEINE = Caffeine.newBuilder()
            // 300并发下3分钟所需数据
            .maximumSize(300 * 3 * 60)
            .expireAfterWrite(3, TimeUnit.MINUTES)
            .build();

    @Override
    public boolean hasConsumed(String messageId, PlatformEnum platform) {
        String key = String.format("%d-%s", platform.getCode(), messageId);
        Integer result = CAFFEINE.asMap()
                .putIfAbsent(key, 1);
        return result != null;
    }
}
