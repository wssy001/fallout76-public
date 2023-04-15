package cyou.wssy001.common.service;

import cyou.wssy001.common.dto.BaseReplyMsgDTO;
import cyou.wssy001.common.enums.PlatformEnum;

import java.util.List;

/**
 * @Description: 消息回复服务类
 * @Author: Tyler
 * @Date: 2023/3/16 13:14
 * @Version: 1.0
 */
public interface ReplyService {
    List<PlatformEnum> getPlatforms();

    // 消息回复，回复方式在 BaseReplyMsg 中
    void reply(BaseReplyMsgDTO msg);

    // 消息回复，回复方式需要自定义
    void reply(BaseReplyMsgDTO msg, Object target);
}
