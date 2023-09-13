package cyou.wssy001.fallout76assistant.kookadapter.listener;

import cyou.wssy001.fallout76assistant.common.enums.EventSourceEnum;
import cyou.wssy001.fallout76assistant.common.service.BaseEventListener;
import cyou.wssy001.fallout76assistant.kookadapter.event.KookEvent;

import java.util.Set;

/**
 * @Description:
 * @Author: Tyler
 * @Date: 2023/9/13 09:21
 * @Version: 1.0
 */
public interface BaseKookEventListener extends BaseEventListener<KookEvent> {

    @Override
    default Set<EventSourceEnum> eventSourceSet() {
        return Set.of(EventSourceEnum.KOOK);
    }
}
