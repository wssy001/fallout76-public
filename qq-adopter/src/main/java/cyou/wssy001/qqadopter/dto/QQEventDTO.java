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
public class QQEventDTO extends BasePlatformEventDTO {
    private Long time;
    private Long selfId;
    private String postType;
    private String messageType;
    private String subType;
    private Integer messageId;
    private Long userId;
    private String message;
    private String rawMessage;
    private Integer font;
    private JSONObject sender;
    private Long targetId;
    private Integer tempSource;
    private Long groupId;
    private JSONObject anonymous;
}

