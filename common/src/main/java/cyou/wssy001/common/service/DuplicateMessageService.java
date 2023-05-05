package cyou.wssy001.common.service;

import cyou.wssy001.common.enums.PlatformEnum;

/**
 * @Description: 重复消息校验类
 * @Author: Tyler
 * @Date: 2023/5/4 15:20
 * @Version: 1.0
 */
public interface DuplicateMessageService {

    boolean hasConsumed(String messageId, PlatformEnum platform);

}
