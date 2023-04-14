package cyou.wssy001.kookadopter.handler;

import cn.hutool.core.util.StrUtil;
import cyou.wssy001.common.dto.BasePlatformEventDTO;
import cyou.wssy001.common.dto.BaseReplyMsgDTO;
import cyou.wssy001.common.entity.BaseEvent;
import cyou.wssy001.common.enums.PlatformEnum;
import cyou.wssy001.common.handler.BaseAdminHandler;
import cyou.wssy001.common.handler.BaseHandler;
import cyou.wssy001.common.handler.BasePrivateHandler;
import cyou.wssy001.kookadopter.dto.KookEventDTO;
import cyou.wssy001.kookadopter.dto.KookReplyMsgDTO;
import cyou.wssy001.kookadopter.enums.KookReplyMsgTemplateEnum;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @Description: Kook帮助指令处理器
 * @Author: Tyler
 * @Date: 2023/4/14 23:57
 * @Version: 1.0
 */
@Component
public class GetHelpKookEventHandler implements BaseHandler {
    private final String msg;


    public GetHelpKookEventHandler(List<BaseHandler> baseHandlers) {
        StringBuffer stringBuffer = new StringBuffer();

        for (BaseHandler baseHandler : baseHandlers) {
            if (baseHandler instanceof BasePrivateHandler) continue;
            if (baseHandler instanceof BaseAdminHandler) continue;
            if (baseHandler.getKeys().contains("/help")) continue;

            stringBuffer.append("`");
            Iterator<String> iterator = baseHandler.getKeys()
                    .iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                stringBuffer.append(key);
                if (iterator.hasNext()) stringBuffer.append("\\t");
            }
            stringBuffer.append("`")
                    .append("\\t\\t")
                    .append(baseHandler.description())
                    .append("\\n");
        }
        msg = stringBuffer.toString();
    }

    @Override
    public PlatformEnum platform() {
        return PlatformEnum.Kook;
    }

    @Override
    public Set<String> getKeys() {
        return Set.of("/help", "/帮助");
    }

    @Override
    public String description() {
        return "获取当前环境下所有可用指令";
    }

    @Override
    public BaseReplyMsgDTO consume(BaseEvent baseEvent, BasePlatformEventDTO basePlatformEventDTO) {
        if (basePlatformEventDTO instanceof KookEventDTO kookEventDTO) {
            String replyMsg;
            String targetId = kookEventDTO.getTargetId();
            if (StrUtil.isBlank(msg)) {
                String format = String.format(KookReplyMsgTemplateEnum.ERROR_MSG_CARD.getMsg(), "暂无帮助内容，请联系管理员添加");
                replyMsg = String.format(KookReplyMsgTemplateEnum.ERROR_MSG.getMsg(), targetId, StringEscapeUtils.escapeJava(format));
            } else {
                String format = String.format(KookReplyMsgTemplateEnum.HELP_MSG_CARD.getMsg(), msg);
                replyMsg = String.format(KookReplyMsgTemplateEnum.CARD_MSG.getMsg(), targetId, StringEscapeUtils.escapeJava(format));
            }

            return new KookReplyMsgDTO()
                    .setApiEndPoint("/api/v3/message/create")
                    .setEventKey(baseEvent.getEventKey())
                    .setMsg(replyMsg);
        }

        return null;
    }
}
