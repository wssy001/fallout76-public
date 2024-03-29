package cyou.wssy001.common.entity;

import cyou.wssy001.common.enums.EventEnum;
import cyou.wssy001.common.enums.PlatformEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Description: 指令触发产生的事件基类
 * @Author: Tyler
 * @Date: 2023/3/16 09:55
 * @Version: 1.0
 */
@Getter
@Setter
@Accessors(chain = true)
public class BaseEvent implements Serializable {
    PlatformEnum platform;

    String eventKey;

    EventEnum eventType = EventEnum.GROUP;
}
