package cyou.wssy001.kookadopter.dto;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.annotation.JSONField;
import cyou.wssy001.common.dto.BasePlatformEventDTO;
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
public class KookEventDTO extends BasePlatformEventDTO {
    private String challenge;
    @JSONField(name = "channel_type")
    private String channelType;

    private String type;
    @JSONField(name = "target_id")
    private String targetId;
    @JSONField(name = "author_id")
    private String authorId;

    private String msgId;

    @JSONField(name = "extra")
    private JSONObject jsonObject;
    @JSONField(name = "verify_token")
    private String verifyToken;

    private String content;
}

