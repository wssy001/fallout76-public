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
public class QQEventDTO extends BasePlatformEventDTO {
    private Long time;
    @JSONField(name = "self_id")
    private Long selfId;
    @JSONField(name = "post_type")
    private String postType;
    @JSONField(name = "message_type")
    private String messageType;
    @JSONField(name = "sub_type")
    private String subType;
    @JSONField(name = "message_id")
    private Integer messageId;
    @JSONField(name = "user_id")
    private Long userId;
    private String message;
    @JSONField(name = "raw_message")
    private String rawMessage;
    private Integer font;
    private JSONObject sender;
    @JSONField(name = "target_id")
    private Long targetId;
    @JSONField(name = "temp_source")
    private Integer tempSource;
    @JSONField(name = "group_id")
    private Long groupId;
    private JSONObject anonymous;
}

