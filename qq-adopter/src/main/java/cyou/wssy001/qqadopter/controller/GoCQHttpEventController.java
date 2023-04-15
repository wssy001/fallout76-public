package cyou.wssy001.qqadopter.controller;

import cyou.wssy001.common.dto.BasePlatformEventDTO;
import cyou.wssy001.common.entity.BaseEvent;
import cyou.wssy001.common.service.HandlerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v2/api/go-cqhttp/")
public class GoCQHttpEventController {
    private final HandlerService handlerService;


    @PostMapping(value = "/webhook", produces = MediaType.APPLICATION_JSON_VALUE)
    @RegisterReflectionForBinding({BaseEvent.class, BasePlatformEventDTO.class})
    public void handleQQEvent(
            @RequestParam(required = false) BaseEvent baseEvent,
            @RequestParam(required = false) BasePlatformEventDTO basePlatformEventDTO
    ) {
        handlerService.handle(baseEvent, basePlatformEventDTO);
    }
}
