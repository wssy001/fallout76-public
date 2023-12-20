package cyou.wssy001.fallout76assistant.kookadapter.publish;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
import cyou.wssy001.fallout76assistant.common.enums.ErrorEnum;
import cyou.wssy001.fallout76assistant.common.event.BaseEvent;
import cyou.wssy001.fallout76assistant.common.exception.CustomException;
import cyou.wssy001.fallout76assistant.common.service.EventPublisher;
import cyou.wssy001.fallout76assistant.kookadapter.event.KookEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @Description:
 * @Author: Tyler
 * @Date: 2023/9/12 16:26
 * @Version: 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class KookEventPublisher implements EventPublisher {
    private final ApplicationContext applicationContext;


    @Async
    @Override
    public void publishEvent(BaseEvent baseEvent) {
        if (baseEvent instanceof KookEvent kookEvent) {
            log.debug("******KookEventPublisher.publishEvent：{}", Thread.currentThread());
            log.debug("******KookEventPublisher.publishEvent：正在发布Kook事件：\n{}", JSON.toJSONString(kookEvent, JSONWriter.Feature.PrettyFormat));
            applicationContext.publishEvent(kookEvent);
        } else {
            throw new CustomException(ErrorEnum.EVENT_NOT_MATCH);
        }
    }
}
