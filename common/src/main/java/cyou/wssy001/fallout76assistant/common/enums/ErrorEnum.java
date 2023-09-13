package cyou.wssy001.fallout76assistant.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description:
 * @Author: Tyler
 * @Date: 2023/9/13 10:11
 * @Version: 1.0
 */
@Getter
@AllArgsConstructor
public enum ErrorEnum {
    EVENT_NOT_MATCH(1, "事件不匹配"),
    ;

    private final int code;
    private final String msg;
}
