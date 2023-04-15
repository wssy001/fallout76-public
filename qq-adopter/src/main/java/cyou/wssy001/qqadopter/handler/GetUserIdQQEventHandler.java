package cyou.wssy001.qqadopter.handler;

import com.alibaba.fastjson2.JSONObject;
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

import java.util.Set;

/**
 * @Description: QQ获取用户ID事件处理器
 * @Author: Tyler
 * @Date: 2023/3/16 10:36
 * @Version: 1.0
 */
@Service
public class GetUserIdQQEventHandler implements BaseHandler {

    @Override
    public PlatformEnum platform() {
        return PlatformEnum.QQ;
    }

    @Override
    public Set<String> getKeys() {
        return Set.of("/获取ID", "/获取id");
    }

    @Override
    public EventEnum eventType() {
        return EventEnum.friend;
    }

    @Override
    public String description() {
        return "获取您的QQ号，可用于添加QQ管理员列表（指令仅限好友聊天）";
    }

    @Override
    public BaseReplyMsgDTO consume(BaseEvent baseEvent, BasePlatformEventDTO basePlatformEventDTO) {
        if (baseEvent instanceof BasePrivateEvent basePrivateEvent && basePlatformEventDTO instanceof QQEventDTO qqEventDTO) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("        ");
            JSONObject sender = qqEventDTO.getSender();
            Long userId = qqEventDTO.getUserId();
            stringBuilder.append(sender.getString("nickname"))
                    .append("    您好：\\n您的QQ号为：")
                    .append(userId);
            String replyMsg = String.format(QQReplyMsgTemplateEnum.PRIVATE_TEXT_MSG.getMsg(), userId, stringBuilder);
            return new QQReplyMsgDTO()
                    .setApiEndPoint("/send_private_msg")
                    .setEventKey(baseEvent.getEventKey())
                    .setMsg(replyMsg);
        }
        return null;
    }
}
