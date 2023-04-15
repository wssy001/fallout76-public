package cyou.wssy001.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * @Description: 消息来源平台的一些常量
 * @Author: Tyler
 * @Date: 2023/3/15 15:46
 * @Version: 1.0
 */
@Getter
@AllArgsConstructor
public enum PlatformEnum {
    QQ(1, "QQ"),
    QQ_GUILD(2, "QQ 频道"),
    Kook(3, "Kook"),
    ;

    private final int code;
    private final String description;
}