package cyou.wssy001.baseserviceprovider.service.impl;

import cyou.wssy001.common.entity.BaseAdminEvent;
import cyou.wssy001.common.entity.BaseEvent;
import cyou.wssy001.common.entity.BasePrivateEvent;
import cyou.wssy001.common.handler.BaseAdminHandler;
import cyou.wssy001.common.handler.BaseHandler;
import cyou.wssy001.common.handler.BasePrivateHandler;
import cyou.wssy001.common.service.HandlerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class HandlerServiceImpl implements HandlerService {
    private final ConcurrentHashMap<String, BaseHandler> publicHandlerMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, BaseHandler> privateHandlerMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, BaseHandler> adminHandlerMap = new ConcurrentHashMap<>();


    public HandlerServiceImpl(List<BaseHandler> handlerList) {
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
    public void handle(BaseEvent baseEvent) {
        String platform = baseEvent.getPlatform()
                .getDescription();
        String key = baseEvent.getEventKey();

        BaseHandler handler = getHandler(baseEvent);
        if (handler == null) {
            log.error("******HandlerServiceImpl.handle：未找到来自指令：{} 在平台：{} 上的handler", key, platform);
            return;
        }

        log.info("******HandlerServiceImpl.handle：正在处理来自平台：{} 的指令：{}", platform, key);
        handler.consume(baseEvent);
    }

    private void putToMap(String key, BaseHandler baseHandler, String mapName) {
        switch (mapName) {
            case "admin" -> adminHandlerMap.put(key, baseHandler);
            case "private" -> privateHandlerMap.put(key, baseHandler);
            default -> publicHandlerMap.put(key, baseHandler);
        }
    }
}
