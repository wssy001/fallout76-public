package cyou.wssy001.kookadopter.dto;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @Description: KookEventDTO
 * @Author: Tyler
 * @Date: 2023/3/16 19:09
 * @Version: 1.0
 */
@Getter
@Setter
@Accessors(chain = true)
public class KookEventDTO {
    private String challenge;

    private String channelType;

    private String type;

    private String targetId;

    private String authorId;

    private String msgId;

    @JSONField(name = "extra")
    private JSONObject jsonObject;

    private String verifyToken;

    private String content;
}

