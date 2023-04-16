package cyou.wssy001.common.entity;

import cyou.wssy001.common.enums.EventEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @Description: 指令触发产生的私人事件基类
 * @Author: Tyler
 * @Date: 2023/3/16 09:55
 * @Version: 1.0
 */
@Getter
@Setter
@Accessors(chain = true)
public class BasePrivateEvent extends BaseEvent {
    private final boolean privateOnly = true;

    private final EventEnum eventType = EventEnum.FRIEND;
}
