package cyou.wssy001.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description: 消息来源平台的一些常量
 * @Author: Tyler
 * @Date: 2023/3/15 15:46
 * @Version: 1.0
 */
@Getter
@AllArgsConstructor
public enum EventEnum {
    GROUP(1, "群"),
    FRIEND(2, "好友"),
    ADMIN(3, "管理员");

    private final int code;
    private final String description;
}
