package cyou.wssy001.qqadopter.dto;

import com.alibaba.fastjson2.JSONObject;
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
    private String postType;
    private String messageType;
    private String subType;
    private String guildId;
    private String channelId;
    private String userId;
    private String messageId;
    private JSONObject sender;
    private String message;
}

