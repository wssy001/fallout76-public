package cyou.wssy001.baseserviceprovider.service.impl;

import cyou.wssy001.common.dto.BasePlatformEventDTO;
import cyou.wssy001.common.dto.BaseReplyMsgDTO;
import cyou.wssy001.common.entity.BaseAdminEvent;
import cyou.wssy001.common.entity.BaseEvent;
import cyou.wssy001.common.entity.BasePrivateEvent;
import cyou.wssy001.common.enums.PlatformEnum;
import cyou.wssy001.common.handler.BaseAdminHandler;
import cyou.wssy001.common.handler.BaseHandler;
import cyou.wssy001.common.handler.BasePrivateHandler;
import cyou.wssy001.common.service.HandlerService;
import cyou.wssy001.common.service.ReplyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class HandlerServiceImpl implements HandlerService {
    private final ConcurrentHashMap<PlatformEnum, ReplyService> replyServiceMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, BaseHandler> publicHandlerMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, BaseHandler> privateHandlerMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, BaseHandler> adminHandlerMap = new ConcurrentHashMap<>();


    public HandlerServiceImpl(List<BaseHandler> handlerList, List<ReplyService> replyServiceList) {
        for (BaseHandler baseHandler : handlerList) {
            int code = baseHandler.platform()
                    .getCode();
            String mapName;
            if (baseHandler instanceof BasePrivateHandler) {
                mapName = "private";
            } else if (baseHandler instanceof BaseAdminHandler) {
                mapName = "admin";
            } else {
                mapName = "public";
            }

            baseHandler.getKeys()
                    .stream()
                    .map(key -> code + "-" + key)
                    .forEach(key -> putToMap(key, baseHandler, mapName));
        }

        for (ReplyService service : replyServiceList) {
            replyServiceMap.put(service.getPlatform(), service);
        }
    }

    private BaseHandler getHandler(BaseEvent baseEvent) {
        String key = baseEvent.getPlatform().getCode() + "-" + baseEvent.getEventKey();

        if (baseEvent instanceof BasePrivateEvent) {
            return privateHandlerMap.get(key);
        } else if (baseEvent instanceof BaseAdminEvent) {
            return adminHandlerMap.get(key);
        } else {
            return publicHandlerMap.get(key);
        }
    }

    @Async
    @Override
    public void handle(BaseEvent baseEvent, BasePlatformEventDTO basePlatformEventDTO) {
        PlatformEnum platform = baseEvent.getPlatform();
        String platformDescription = platform
                .getDescription();
        String key = baseEvent.getEventKey();

        BaseHandler handler = getHandler(baseEvent);
        if (handler == null) {
            log.error("******HandlerServiceImpl.handle：未找到来自指令：{} 在平台：{} 上的handler", key, platformDescription);
            return;
        }

        log.info("******HandlerServiceImpl.handle：正在处理来自平台：{} 的指令：{}", platformDescription, key);
        BaseReplyMsgDTO baseReplyMsgDTO = handler.consume(baseEvent, basePlatformEventDTO);
        if (baseReplyMsgDTO == null) return;

        ReplyService replyService = replyServiceMap.get(platform);
        if (replyService == null) return;
        replyService.reply(baseReplyMsgDTO);
    }

    private void putToMap(String key, BaseHandler baseHandler, String mapName) {
        switch (mapName) {
            case "admin" -> adminHandlerMap.put(key, baseHandler);
            case "private" -> privateHandlerMap.put(key, baseHandler);
            default -> publicHandlerMap.put(key, baseHandler);
        }
    }
}
