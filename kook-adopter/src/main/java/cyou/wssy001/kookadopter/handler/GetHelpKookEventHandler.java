package cyou.wssy001.kookadopter.handler;

import cn.hutool.core.util.StrUtil;
import cyou.wssy001.common.dto.BasePlatformEventDTO;
import cyou.wssy001.common.dto.BaseReplyMsgDTO;
import cyou.wssy001.common.entity.BaseEvent;
import cyou.wssy001.common.enums.EventEnum;
import cyou.wssy001.common.enums.PlatformEnum;
import cyou.wssy001.common.handler.BaseHandler;
import cyou.wssy001.common.handler.BaseHelpHandler;
import cyou.wssy001.kookadopter.dto.KookEventDTO;
import cyou.wssy001.kookadopter.dto.KookReplyMsgDTO;
import cyou.wssy001.kookadopter.enums.KookReplyMsgTemplateEnum;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;

/**
 * @Description: Kook帮助指令处理器
 * @Author: Tyler
 * @Date: 2023/4/14 23:57
 * @Version: 1.0
 */
@Component
public class GetHelpKookEventHandler implements BaseHelpHandler {
    private final String msg;


    public GetHelpKookEventHandler(List<BaseHandler> baseHandlers) {
        StringBuilder stringBuilder = new StringBuilder();

        for (BaseHandler baseHandler : baseHandlers) {
            if (baseHandler.getKeys().contains("/help")) continue;
            if (!baseHandler.getPlatform().equals(PlatformEnum.KOOK)) continue;
            if (!baseHandler.getEventType().equals(EventEnum.GROUP)) continue;

            stringBuilder.append("`");
            Iterator<String> iterator = baseHandler.getKeys()
                    .iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                stringBuilder.append(key);
                if (iterator.hasNext()) stringBuilder.append("\\t");
            }
            stringBuilder.append("`")
                    .append("\\t\\t")
                    .append(baseHandler.getDescription())
                    .append("\\n");
        }
        msg = stringBuilder.toString();
    }

    @Override
    public PlatformEnum getPlatform() {
        return PlatformEnum.KOOK;
    }

    @Override
    public EventEnum getEventType() {
        return EventEnum.GROUP;
    }

    @Override
    public BaseReplyMsgDTO consume(BaseEvent baseEvent, BasePlatformEventDTO basePlatformEventDTO) {
        if (basePlatformEventDTO instanceof KookEventDTO kookEventDTO) {
            String targetId = kookEventDTO.getTargetId();
            String format;
            if (StrUtil.isBlank(msg)) {
                format = String.format(KookReplyMsgTemplateEnum.ERROR_MSG_CARD.getMsg(), "暂无帮助内容，请联系管理员添加");
            } else {
                format = String.format(KookReplyMsgTemplateEnum.HELP_MSG_CARD.getMsg(), msg);
            }

            String replyMsg = String.format(KookReplyMsgTemplateEnum.CARD_MSG.getMsg(), targetId, StringEscapeUtils.escapeJava(format));
            return new KookReplyMsgDTO()
                    .setApiEndPoint("/api/v3/message/create")
                    .setEventKey(baseEvent.getEventKey())
                    .setMsg(replyMsg);
        }

        return null;
    }
}
