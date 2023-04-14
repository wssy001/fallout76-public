package cyou.wssy001.common.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @Description: 消息回复基类
 * @Author: Tyler
 * @Date: 2023/3/16 14:27
 * @Version: 1.0
 */
@Getter
@Setter
@Accessors(chain = true)
public abstract class BaseReplyMsg {
    // 出发的指令
    private String eventKey;

    //封装好的回复消息
    private String msg;
}
