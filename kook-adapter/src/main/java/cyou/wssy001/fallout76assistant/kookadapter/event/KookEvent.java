package cyou.wssy001.fallout76assistant.kookadapter.event;

import cyou.wssy001.fallout76assistant.common.enums.EventSourceEnum;
import cyou.wssy001.fallout76assistant.common.event.BaseEvent;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @Description: 基础Kook事件
 * @Author: Tyler
 * @Date: 2023/9/13 09:22
 * @Version: 1.0
 */
@Getter
@Setter
@Accessors(chain = true)
public class KookEvent extends BaseEvent {

    private String channelId;
    private String userId;

    public KookEvent() {
        super(KookEvent.class);
    }

    @Override
    public EventSourceEnum getEventSource() {
        return EventSourceEnum.KOOK;
    }
}
