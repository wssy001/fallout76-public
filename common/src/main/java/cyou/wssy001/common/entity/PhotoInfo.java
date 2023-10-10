package cyou.wssy001.common.entity;

import com.alibaba.fastjson2.annotation.JSONField;
import cyou.wssy001.common.enums.PlatformEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Map;

@Getter
@Setter
@Accessors(chain = true)
public class PhotoInfo implements Serializable {
    @JSONField(name = "code")
    private PlatformEnum platform;
    private String key;
    private Map<String, String> urlMap;
}
