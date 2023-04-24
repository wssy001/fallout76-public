package cyou.wssy001.common.entity;

import com.alibaba.fastjson2.annotation.JSONField;
import cyou.wssy001.common.enums.PlatformEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.LinkedHashMap;

@Getter
@Setter
@Accessors(chain = true)
public class PhotoInfo {
    @JSONField(name = "code")
    private PlatformEnum platform;
    private String key;
    private LinkedHashMap<String, String> urlMap;

    public void setPlatform(int code) {
        for (PlatformEnum platform : PlatformEnum.values()) {
            if (platform.getCode() == code) {
                this.platform = platform;
                return;
            }
        }
    }
}
