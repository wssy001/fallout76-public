package cyou.wssy001.qqadopter.dto;

import com.alibaba.fastjson2.JSONObject;
import cyou.wssy001.common.dto.BasePlatformEventDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @Description: QQFileUploadEventDTO
 * @Author: Tyler
 * @Date: 2023/5/17 9:31
 * @Version: 1.0
 */
@Getter
@Setter
@Accessors(chain = true)
public class QQFileUploadEventDTO extends BasePlatformEventDTO {
    private String postType;
    private String noticeType;
    private long groupId;
    private long userId;
    private JSONObject file;
}
