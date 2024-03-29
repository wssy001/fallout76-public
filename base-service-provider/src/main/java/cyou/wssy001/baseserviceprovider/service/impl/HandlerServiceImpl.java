package cyou.wssy001.baseserviceprovider.service.impl;

import cyou.wssy001.common.dto.BasePlatformEventDTO;
import cyou.wssy001.common.dto.BaseReplyMsgDTO;
import cyou.wssy001.common.entity.BaseEvent;
import cyou.wssy001.common.enums.EventEnum;
import cyou.wssy001.common.enums.PlatformEnum;
import cyou.wssy001.common.handler.BaseHandler;
import cyou.wssy001.common.handler.BaseHelpHandler;
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
    private static final ConcurrentHashMap<PlatformEnum, ReplyService> replyServiceMap = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, BaseHandler> publicHandlerMap = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, BaseHandler> privateHandlerMap = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, BaseHandler> adminHandlerMap = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, BaseHelpHandler> helpHandlerMap = new ConcurrentHashMap<>();


    public HandlerServiceImpl(List<BaseHandler> handlerList, List<ReplyService> replyServiceList, List<BaseHelpHandler> baseHelpHandlers) {
        handlerList.forEach(this::stockHandler);
        baseHelpHandlers.forEach(this::stockHandler);

        for (ReplyService service : replyServiceList) {
            service.getPlatforms()
                    .forEach(platformEnum -> replyServiceMap.put(platformEnum, service));
        }
    }

    @Async
    @Override
    public void handle(BaseEvent baseEvent, BasePlatformEventDTO basePlatformEventDTO) {
        PlatformEnum platform = baseEvent.getPlatform();
        String platformDescription = platform
                .getDescription();
        String key = baseEvent.getEventKey();

        BaseReplyMsgDTO baseReplyMsgDTO;
        if (key.equals("/help") || key.equals("/帮助")) {
            BaseHelpHandler helpHandler = getHelpHandler(baseEvent);
            if (helpHandler == null) {
                log.error("******HandlerServiceImpl.handle：未找到来自指令：{} 在平台：{} 上的handler", key, platformDescription);
                return;
            }
            log.info("******HandlerServiceImpl.handle：正在处理来自平台：{} 的指令：{}", platformDescription, key);
            baseReplyMsgDTO = helpHandler.consume(baseEvent, basePlatformEventDTO);
        } else {
            BaseHandler handler = getHandler(baseEvent);
            if (handler == null) {
                log.error("******HandlerServiceImpl.handle：未找到来自指令：{} 在平台：{} 上的handler", key, platformDescription);
                return;
            }

            log.info("******HandlerServiceImpl.handle：正在处理来自平台：{} 的指令：{}", platformDescription, key);
            baseReplyMsgDTO = handler.consume(baseEvent, basePlatformEventDTO);
        }
        if (baseReplyMsgDTO == null) return;

        ReplyService replyService = replyServiceMap.get(platform);
        if (replyService == null) return;
        replyService.reply(baseReplyMsgDTO);
    }

    private void stockHandler(BaseHandler baseHandler) {
        int platformCode = baseHandler.getPlatform()
                .getCode();
        EventEnum eventType = baseHandler.getEventType();
        int eventTypeCode = eventType.getCode();
        String mapName;
        switch (eventType) {
            case FRIEND -> mapName = "private";
            case ADMIN -> mapName = "admin";
            default -> mapName = "public";
        }

        baseHandler.getKeys()
                .stream()
                .map(key -> String.format("%d-%s-%d", platformCode, key, eventTypeCode))
                .forEach(key -> putToMap(key, baseHandler, mapName));
    }

    private void stockHandler(BaseHelpHandler baseHelpHandler) {
        int platformCode = baseHelpHandler.getPlatform()
                .getCode();
        EventEnum eventType = baseHelpHandler.getEventType();
        int eventTypeCode = eventType.getCode();
        baseHelpHandler.getKeys()
                .stream()
                .map(key -> String.format("%d-%s-%d", platformCode, key, eventTypeCode))
                .forEach(key -> putToMap(key, baseHelpHandler, "help"));
    }

    private BaseHandler getHandler(BaseEvent baseEvent) {
        String eventKey = baseEvent.getEventKey();
        int platformCode = baseEvent.getPlatform()
                .getCode();
        EventEnum eventType = baseEvent.getEventType();
        int eventTypeCode = eventType.getCode();
        String key = String.format("%d-%s-%d", platformCode, eventKey, eventTypeCode);

        switch (eventType) {
            case FRIEND -> {
                return privateHandlerMap.get(key);
            }
            case ADMIN -> {
                return adminHandlerMap.get(key);
            }
            default -> {
                return publicHandlerMap.get(key);
            }
        }

    }

    private BaseHelpHandler getHelpHandler(BaseEvent baseEvent) {
        String eventKey = baseEvent.getEventKey();
        int platformCode = baseEvent.getPlatform()
                .getCode();
        EventEnum eventType = baseEvent.getEventType();
        int eventTypeCode = eventType.getCode();
        String key = String.format("%d-%s-%d", platformCode, eventKey, eventTypeCode);
        return helpHandlerMap.get(key);
    }

    private void putToMap(String key, Object handler, String mapName) {
        switch (mapName) {
            case "admin" -> adminHandlerMap.put(key, (BaseHandler) handler);
            case "private" -> privateHandlerMap.put(key, (BaseHandler) handler);
            case "help" -> helpHandlerMap.put(key, (BaseHelpHandler) handler);
            default -> publicHandlerMap.put(key, (BaseHandler) handler);
        }
    }
}
