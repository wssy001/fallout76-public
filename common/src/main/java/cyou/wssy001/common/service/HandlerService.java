package cyou.wssy001.common.service;

import cyou.wssy001.common.dto.BasePlatformEventDTO;
import cyou.wssy001.common.entity.BaseEvent;

public interface HandlerService {

    void handle(BaseEvent baseEvent, BasePlatformEventDTO basePlatformEventDTO);
}
