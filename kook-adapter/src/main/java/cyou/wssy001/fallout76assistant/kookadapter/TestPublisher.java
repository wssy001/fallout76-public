package cyou.wssy001.fallout76assistant.kookadapter;

import cn.hutool.core.util.IdUtil;
import cyou.wssy001.fallout76assistant.common.enums.EventTypeEnum;
import cyou.wssy001.fallout76assistant.common.event.BaseEvent;
import cyou.wssy001.fallout76assistant.kookadapter.event.KookEvent;
import cyou.wssy001.fallout76assistant.kookadapter.publish.KookEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @Description:
 * @Author: Tyler
 * @Date: 2023/9/13 09:55
 * @Version: 1.0
 */
@Component
@RequiredArgsConstructor
public class TestPublisher implements ApplicationListener<ApplicationReadyEvent> {
    private final KookEventPublisher kookEventPublisher;


    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        BaseEvent baseEvent = new KookEvent()
                .setUserId(IdUtil.getSnowflakeNextIdStr())
                .setChannelId(IdUtil.getSnowflakeNextIdStr())
                .setEventType(EventTypeEnum.PRIVATE)
                .setKey("/b")
                .setMsg("/b sagdghjas");
        kookEventPublisher.publishEvent(baseEvent);
    }
}
