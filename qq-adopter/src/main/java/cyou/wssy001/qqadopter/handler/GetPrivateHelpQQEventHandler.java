package cyou.wssy001.qqadopter.handler;

import cn.hutool.core.util.StrUtil;
import cyou.wssy001.common.dto.BasePlatformEventDTO;
import cyou.wssy001.common.dto.BaseReplyMsgDTO;
import cyou.wssy001.common.entity.BaseEvent;
import cyou.wssy001.common.entity.BasePrivateEvent;
import cyou.wssy001.common.enums.EventEnum;
import cyou.wssy001.common.enums.PlatformEnum;
import cyou.wssy001.common.handler.BaseHandler;
import cyou.wssy001.common.handler.BaseHelpHandler;
import cyou.wssy001.qqadopter.dto.QQEventDTO;
import cyou.wssy001.qqadopter.dto.QQReplyMsgDTO;
import cyou.wssy001.qqadopter.enums.QQReplyMsgTemplateEnum;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;

/**
 * @Description: QQ私人帮助指令处理器
 * @Author: Tyler
 * @Date: 2023/4/14 23:57
 * @Version: 1.0
 */
@Service
public class GetPrivateHelpQQEventHandler implements BaseHelpHandler {
    private final String msg;


    public GetPrivateHelpQQEventHandler(List<BaseHandler> baseHandlers) {
        StringBuilder stringBuilder = new StringBuilder();

        for (BaseHandler baseHandler : baseHandlers) {
            if (baseHandler.getKeys().contains("/help")) continue;
            if (!baseHandler.getPlatform().equals(PlatformEnum.QQ)) continue;
            if (!baseHandler.getEventType().equals(EventEnum.FRIEND)) continue;

            Iterator<String> iterator = baseHandler.getKeys()
                    .iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                stringBuilder.append(key);
                if (iterator.hasNext()) stringBuilder.append("\\t");
            }
            stringBuilder.append("\\t\\t")
                    .append(baseHandler.getDescription())
                    .append("\\n");
        }
        msg = stringBuilder.toString();
    }

    @Override
    public PlatformEnum getPlatform() {
        return PlatformEnum.QQ;
    }

    @Override
    public EventEnum getEventType() {
        return EventEnum.FRIEND;
    }

    @Override
    public BaseReplyMsgDTO consume(BaseEvent baseEvent, BasePlatformEventDTO basePlatformEventDTO) {
        if (baseEvent instanceof BasePrivateEvent && basePlatformEventDTO instanceof QQEventDTO qqEventDTO) {
            String format;
            Long userId = qqEventDTO.getUserId();
            if (StrUtil.isBlank(msg)) {
                format = String.format(QQReplyMsgTemplateEnum.TEXT_MSG_TEMPLATE.getMsg(), "暂无帮助内容，请联系管理员添加");
            } else {
                format = String.format(QQReplyMsgTemplateEnum.TEXT_MSG_TEMPLATE.getMsg(), msg);
            }

            String replyMsg = String.format(QQReplyMsgTemplateEnum.PRIVATE_TEXT_MSG.getMsg(), userId, format);
            return new QQReplyMsgDTO()
                    .setApiEndPoint("/send_private_msg")
                    .setEventKey(baseEvent.getEventKey())
                    .setMsg(replyMsg);
        }

        return null;
    }
}
