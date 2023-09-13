package cyou.wssy001.fallout76assistant.common.service;

import cyou.wssy001.fallout76assistant.common.enums.EventSourceEnum;
import cyou.wssy001.fallout76assistant.common.enums.EventTypeEnum;
import cyou.wssy001.fallout76assistant.common.event.BaseEvent;

import java.util.Set;

/**
 * @Description:
 * @Author: Tyler
 * @Date: 2023/9/13 09:13
 * @Version: 1.0
 */
public interface BaseEventListener<E extends BaseEvent> {

    Set<String> keySet();

    Set<EventSourceEnum> eventSourceSet();

    Set<EventTypeEnum> eventTypeSet();

    default boolean match(BaseEvent baseEvent) {
        return keySet().contains(baseEvent.getKey()) &&
                eventSourceSet().contains(baseEvent.getEventSource()) &&
                eventTypeSet().contains(baseEvent.getEventType());
    }

    void handleEvent(E event);
}
