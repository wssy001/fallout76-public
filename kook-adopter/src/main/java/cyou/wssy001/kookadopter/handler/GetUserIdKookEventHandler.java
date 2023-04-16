package cyou.wssy001.kookadopter.handler;

import com.alibaba.fastjson2.JSONObject;
import cyou.wssy001.common.dto.BasePlatformEventDTO;
import cyou.wssy001.common.dto.BaseReplyMsgDTO;
import cyou.wssy001.common.entity.BaseEvent;
import cyou.wssy001.common.entity.BasePrivateEvent;
import cyou.wssy001.common.enums.EventEnum;
import cyou.wssy001.common.enums.PlatformEnum;
import cyou.wssy001.common.handler.BaseHandler;
import cyou.wssy001.kookadopter.dto.KookEventDTO;
import cyou.wssy001.kookadopter.dto.KookReplyMsgDTO;
import cyou.wssy001.kookadopter.enums.KookReplyMsgTemplateEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * @Description: Kook获取用户ID事件处理器
 * @Author: Tyler
 * @Date: 2023/3/16 10:36
 * @Version: 1.0
 */
@Service
@RequiredArgsConstructor
public class GetUserIdKookEventHandler implements BaseHandler {
    @Override
    public PlatformEnum platform() {
        return PlatformEnum.KOOK;
    }

    @Override
    public EventEnum eventType() {
        return EventEnum.FRIEND;
    }

    @Override
    public Set<String> getKeys() {
        return Set.of("/获取ID", "/获取id");
    }

    @Override
    public String description() {
        return "获取您在Kook的Open Id，可用于添加Kook管理员列表（指令仅限私聊）";
    }

    @Override
    public BaseReplyMsgDTO consume(BaseEvent baseEvent, BasePlatformEventDTO basePlatformEventDTO) {
        if (baseEvent instanceof BasePrivateEvent basePrivateEvent && basePlatformEventDTO instanceof KookEventDTO kookEventDTO) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("        ");
            JSONObject author = kookEventDTO.getJsonObject()
                    .getJSONObject("author");
            stringBuilder.append(author.getString("username"))
                    .append("#")
                    .append(author.getString("identify_num"))
                    .append("    您好：\\n您在 Kook 的 Open ID 为：")
                    .append(kookEventDTO.getAuthorId());
            String replyMsg = String.format(KookReplyMsgTemplateEnum.TEXT_MSG.getMsg(), kookEventDTO.getAuthorId(), stringBuilder);
            return new KookReplyMsgDTO()
                    .setApiEndPoint("/api/v3/direct-message/create")
                    .setEventKey(baseEvent.getEventKey())
                    .setMsg(replyMsg);
        }
        return null;
    }
}
