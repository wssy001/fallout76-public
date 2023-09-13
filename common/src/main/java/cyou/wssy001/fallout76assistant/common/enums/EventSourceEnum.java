package cyou.wssy001.fallout76assistant.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description: 事件来源枚举
 * @Author: Tyler
 * @Date: 2023/9/12 13:10
 * @Version: 1.0
 */
@Getter
@AllArgsConstructor
public enum EventSourceEnum {
    QQ(1, "QQ"),
    QQ_GUILD(2, "QQ频道"),
    KOOK(3, "Kook"),
    DODO(3, "DoDo"),
    ;

    private final int code;
    private final String description;
}
