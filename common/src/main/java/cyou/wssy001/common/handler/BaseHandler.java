package cyou.wssy001.common.handler;

import cyou.wssy001.common.dto.BasePlatformEventDTO;
import cyou.wssy001.common.dto.BaseReplyMsgDTO;
import cyou.wssy001.common.entity.BaseEvent;
import cyou.wssy001.common.enums.PlatformEnum;

import java.util.Set;

public interface BaseHandler {

    // 事件触发的平台
    PlatformEnum platform();

    // 事件触发的关键字集合
    Set<String> getKeys();

    // 描述
    String description();

    // 消费
    BaseReplyMsgDTO consume(BaseEvent baseEvent, BasePlatformEventDTO basePlatformEventDTO);
}
