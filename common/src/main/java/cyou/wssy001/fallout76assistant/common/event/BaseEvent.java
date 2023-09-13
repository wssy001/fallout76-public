package cyou.wssy001.fallout76assistant.common.event;

import cyou.wssy001.fallout76assistant.common.enums.EventSourceEnum;
import cyou.wssy001.fallout76assistant.common.enums.EventTypeEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.context.ApplicationEvent;

import java.util.List;
import java.util.Set;

/**
 * @Description: 基础事件
 * @Author: Tyler
 * @Date: 2023/9/12 13:10
 * @Version: 1.0
 */
@Getter
@Setter
@Accessors(chain = true)
public abstract class BaseEvent extends ApplicationEvent {
    private EventTypeEnum eventType;
    private EventSourceEnum eventSource;
    private String msg;
    private String key;

    public BaseEvent(Object source) {
        super(source);
    }

}
