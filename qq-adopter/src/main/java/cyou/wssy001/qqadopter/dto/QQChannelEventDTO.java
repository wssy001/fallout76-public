package cyou.wssy001.qqadopter.dto;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.annotation.JSONField;
import cyou.wssy001.common.dto.BasePlatformEventDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @Description: QQEventDTO
 * @Author: Tyler
 * @Date: 2023/3/16 19:09
 * @Version: 1.0
 */
@Getter
@Setter
@Accessors(chain = true)
public class QQChannelEventDTO extends BasePlatformEventDTO {
    @JSONField(name = "post_type")
    private String postType;
    @JSONField(name = "message_type")
    private String messageType;
    @JSONField(name = "sub_type")
    private String subType;
    @JSONField(name = "guild_id")
    private String guildId;
    @JSONField(name = "channel_id")
    private String channelId;
    @JSONField(name = "user_id")
    private String userId;
    @JSONField(name = "message_id")
    private String messageId;
    private JSONObject sender;
    private String message;
}

