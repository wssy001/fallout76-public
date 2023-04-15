package cyou.wssy001.common.handler;

import cyou.wssy001.common.dto.BasePlatformEventDTO;
import cyou.wssy001.common.dto.BaseReplyMsgDTO;
import cyou.wssy001.common.entity.BaseEvent;
import cyou.wssy001.common.enums.EventEnum;
import cyou.wssy001.common.enums.PlatformEnum;

import java.util.Set;

public interface BaseHelpHandler {

    // 事件触发的平台
    PlatformEnum platform();

    // 事件触发的关键字集合
    default Set<String> getKeys() {
        return Set.of("/help", "/帮助");
    }

    EventEnum eventType();

    // 描述
    default String description() {
        return "获取当前环境下所有可用指令";
    }

    // 消费
    BaseReplyMsgDTO consume(BaseEvent baseEvent, BasePlatformEventDTO basePlatformEventDTO);
}
