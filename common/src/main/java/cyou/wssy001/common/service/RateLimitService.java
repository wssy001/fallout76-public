package cyou.wssy001.common.service;

import cyou.wssy001.common.enums.EventEnum;
import cyou.wssy001.common.enums.PlatformEnum;

/**
 * @Description: API限速
 * @Author: Tyler
 * @Date: 2023/4/28 9:10
 * @Version: 1.0
 */
public interface RateLimitService {

    boolean hasRemain(String user, String eventKey, PlatformEnum platform);

    boolean hasRemain(String remoteEndpoint, PlatformEnum platform, EventEnum eventType);

    void updateRemoteEndpointLimit(String remoteEndpoint, int resetTime, PlatformEnum platform);

}
