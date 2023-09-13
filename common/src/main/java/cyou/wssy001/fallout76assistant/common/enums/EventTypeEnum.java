package cyou.wssy001.fallout76assistant.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description: 事件类型枚举
 * @Author: Tyler
 * @Date: 2023/3/15 15:46
 * @Version: 1.0
 */
@Getter
@AllArgsConstructor
public enum EventTypeEnum {
    GROUP(1, "群"),
    GUILD(2, "频道"),
    PRIVATE(3, "私信"),
    FRIEND(4, "好友"),
    ADMIN(5, "管理员");

    private final int code;
    private final String description;
}
