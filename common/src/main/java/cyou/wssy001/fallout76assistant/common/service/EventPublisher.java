package cyou.wssy001.fallout76assistant.common.service;

import cyou.wssy001.fallout76assistant.common.event.BaseEvent;

/**
 * @Description:
 * @Author: Tyler
 * @Date: 2023/9/11 15:51
 * @Version: 1.0
 */
public interface EventPublisher{
    void publishEvent(BaseEvent event);
}
