package cyou.wssy001.dodoadopter.controller;

import cyou.wssy001.common.dto.BasePlatformEventDTO;
import cyou.wssy001.common.entity.BaseEvent;
import cyou.wssy001.common.service.HandlerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v2/api/dodo/")
public class DoDoHttpEventController {
    private final HandlerService handlerService;


    @PostMapping(value = "/webhook", produces = MediaType.APPLICATION_JSON_VALUE)
    public void handleDoDoEvent(
            @RequestParam(required = false) BaseEvent baseEvent,
            @RequestParam(required = false) BasePlatformEventDTO basePlatformEventDTO
    ) {
        handlerService.handle(baseEvent, basePlatformEventDTO);
    }
}
