package cyou.wssy001.dodoadopter.dto;

import com.alibaba.fastjson2.JSONObject;
import cyou.wssy001.common.dto.BasePlatformEventDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @Description: DoDoEventDTO
 * @Author: Tyler
 * @Date: 2023/3/16 19:09
 * @Version: 1.0
 */
@Getter
@Setter
@Accessors(chain = true)
public class DoDoEventDTO extends BasePlatformEventDTO {
    private int type;
    private EventBody data;
    private String version;

    @Getter
    @Setter
    public static class EventBody {
        private JSONObject eventBody;
        private String eventId;
        private String eventType;
        private long timestamp;
    }
}

