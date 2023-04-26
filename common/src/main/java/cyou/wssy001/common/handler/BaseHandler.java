package cyou.wssy001.common.handler;

import cyou.wssy001.common.dto.BasePlatformEventDTO;
import cyou.wssy001.common.dto.BaseReplyMsgDTO;
import cyou.wssy001.common.entity.BaseEvent;
import cyou.wssy001.common.enums.EventEnum;
import cyou.wssy001.common.enums.PlatformEnum;

import java.util.Set;

public interface BaseHandler {

    // 事件触发的平台
    PlatformEnum getPlatform();

    // 事件类型
    EventEnum getEventType();

    // 事件触发的关键字集合
    Set<String> getKeys();

    // 描述
    String getDescription();

    // 消费
    BaseReplyMsgDTO consume(BaseEvent baseEvent, BasePlatformEventDTO basePlatformEventDTO);
}
