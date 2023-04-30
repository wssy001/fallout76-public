package cyou.wssy001.common.enums;

import com.alibaba.fastjson2.annotation.JSONCreator;
import com.alibaba.fastjson2.annotation.JSONField;
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
public enum PlatformEnum {
    QQ(1, "QQ"),
    QQ_GUILD(2, "QQ 频道"),
    KOOK(3, "Kook"),
    DODO(4, "DoDo");

    private final int code;
    private final String description;

    @JSONField
    public int getCode() {
        return code;
    }

    @JSONCreator
    public static PlatformEnum from(int code) {
        for (PlatformEnum v : values()) {
            if (v.code == code) {
                return v;
            }
        }

        throw new IllegalArgumentException("code " + code);
    }
}
