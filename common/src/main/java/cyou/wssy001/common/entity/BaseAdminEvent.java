package cyou.wssy001.common.entity;

import cyou.wssy001.common.enums.EventEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @Description: 指令触发产生的管理员事件基类
 * @Author: Tyler
 * @Date: 2023/3/16 09:55
 * @Version: 1.0
 */
@Getter
@Setter
@Accessors(chain = true)
public class BaseAdminEvent extends BaseEvent {
    private final boolean adminOnly = true;

    private final EventEnum eventType = EventEnum.ADMIN;
}
