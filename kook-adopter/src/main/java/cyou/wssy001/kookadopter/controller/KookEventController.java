package cyou.wssy001.kookadopter.controller;

import cyou.wssy001.common.entity.BaseEvent;
import cyou.wssy001.common.service.HandlerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class KookEventController {
    private final HandlerService handlerService;


    @PostMapping("/webhook")
    public void handleKookEvent(
            BaseEvent baseEvent
    ) {
        handlerService.handle(baseEvent);
    }
}
