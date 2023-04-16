package cyou.wssy001.dodoadopter.dto;

import cyou.wssy001.common.dto.BaseReplyMsgDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @Description: DoDo消息回复类
 * @Author: Tyler
 * @Date: 2023/3/16 14:28
 * @Version: 1.0
 */
@Getter
@Setter
@Accessors(chain = true)
public class DoDoReplyMsgDTO extends BaseReplyMsgDTO {
    private String apiEndPoint;
}
