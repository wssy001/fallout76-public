package cyou.wssy001.fallout76assistant.kookadapter.listener;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
import cyou.wssy001.fallout76assistant.common.enums.EventTypeEnum;
import cyou.wssy001.fallout76assistant.kookadapter.event.KookEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @Description:
 * @Author: Tyler
 * @Date: 2023/9/11 15:54
 * @Version: 1.0
 */
@Slf4j
@Component
public class GetUserIdListener implements BaseKookEventListener {

    @Override
    public Set<String> keySet() {
        return Set.of("/a", "/b");
    }

    @Override
    public Set<EventTypeEnum> eventTypeSet() {
        return Set.of(EventTypeEnum.PRIVATE, EventTypeEnum.ADMIN);
    }

    @Override
    @EventListener(condition = "@getUserIdListener.match(#root.args[0])")
    public void handleEvent(KookEvent kookEvent) {
        log.debug("******GetUserIdListener.handleEvent：{}", Thread.currentThread());
        log.debug("******GetUserIdListener.handleEvent：正在处理事件：\n{}", JSON.toJSONString(kookEvent, JSONWriter.Feature.PrettyFormat));
    }

}
