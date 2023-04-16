package cyou.wssy001.dodoadopter.handler;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONObject;
import cyou.wssy001.common.dto.BasePlatformEventDTO;
import cyou.wssy001.common.dto.BaseReplyMsgDTO;
import cyou.wssy001.common.entity.BaseAdminEvent;
import cyou.wssy001.common.entity.BaseEvent;
import cyou.wssy001.common.enums.EventEnum;
import cyou.wssy001.common.enums.PlatformEnum;
import cyou.wssy001.common.handler.BaseHandler;
import cyou.wssy001.common.handler.BaseHelpHandler;
import cyou.wssy001.dodoadopter.dto.DoDoEventDTO;
import cyou.wssy001.dodoadopter.dto.DoDoReplyMsgDTO;
import cyou.wssy001.dodoadopter.enums.DoDoReplyMsgTemplateEnum;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @Description: DoDo管理员帮助指令处理器
 * @Author: Tyler
 * @Date: 2023/4/14 23:57
 * @Version: 1.0
 */
@Component
public class GetAdminHelpDoDoEventHandler implements BaseHelpHandler {
    private final String msg;


    public GetAdminHelpDoDoEventHandler(List<BaseHandler> baseHandlers) {
        StringBuilder stringBuilder = new StringBuilder();

        for (BaseHandler baseHandler : baseHandlers) {
            if (baseHandler.getKeys().contains("/help")) continue;
            if (!baseHandler.platform().equals(PlatformEnum.DODO)) continue;
            if (!baseHandler.eventType().equals(EventEnum.ADMIN)) continue;

            Iterator<String> iterator = baseHandler.getKeys()
                    .iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                stringBuilder.append(key);
                if (iterator.hasNext()) stringBuilder.append("\\t");
            }
            stringBuilder.append("\\t\\t")
                    .append(baseHandler.description())
                    .append("\\n");
        }
        msg = stringBuilder.toString();
    }

    @Override
    public PlatformEnum platform() {
        return PlatformEnum.DODO;
    }

    @Override
    public Set<String> getKeys() {
        return Set.of("/help", "/帮助");
    }

    @Override
    public EventEnum eventType() {
        return EventEnum.ADMIN;
    }

    @Override
    public String description() {
        return "获取当前环境下所有可用指令";
    }

    @Override
    public BaseReplyMsgDTO consume(BaseEvent baseEvent, BasePlatformEventDTO basePlatformEventDTO) {
        if (baseEvent instanceof BaseAdminEvent && basePlatformEventDTO instanceof DoDoEventDTO dodoEventDTO) {
            String eventKey = baseEvent.getEventKey();
            DoDoEventDTO.EventBody dodoEventDTOData = dodoEventDTO.getData();
            JSONObject eventBody = dodoEventDTOData.getEventBody();
            String islandSourceId = eventBody.getString("islandSourceId");
            String dodoSourceId = eventBody.getString("dodoSourceId");
            String replyMsg;
            if (StrUtil.isBlank(msg)) {
                String format = String.format(DoDoReplyMsgTemplateEnum.TEXT_MSG_TEMPLATE.getMsg(), "暂无帮助内容，请联系管理员添加");
                replyMsg = String.format(DoDoReplyMsgTemplateEnum.PRIVATE_TEXT_MSG.getMsg(), islandSourceId, dodoSourceId, format);
            } else {
                String format = String.format(DoDoReplyMsgTemplateEnum.TEXT_MSG_TEMPLATE.getMsg(), msg);
                replyMsg = String.format(DoDoReplyMsgTemplateEnum.PRIVATE_TEXT_MSG.getMsg(), islandSourceId, dodoSourceId, format);
            }

            return new DoDoReplyMsgDTO()
                    .setApiEndPoint("/api/v2/personal/message/send")
                    .setEventKey(eventKey)
                    .setMsg(replyMsg);
        }

        return null;
    }
}
