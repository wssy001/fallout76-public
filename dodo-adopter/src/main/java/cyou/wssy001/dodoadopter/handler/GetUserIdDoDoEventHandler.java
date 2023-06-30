package cyou.wssy001.dodoadopter.handler;

import com.alibaba.fastjson2.JSONObject;
import cyou.wssy001.common.dto.BasePlatformEventDTO;
import cyou.wssy001.common.dto.BaseReplyMsgDTO;
import cyou.wssy001.common.entity.BaseEvent;
import cyou.wssy001.common.entity.BasePrivateEvent;
import cyou.wssy001.common.enums.EventEnum;
import cyou.wssy001.common.enums.PlatformEnum;
import cyou.wssy001.common.handler.BaseHandler;
import cyou.wssy001.dodoadopter.dto.DoDoEventDTO;
import cyou.wssy001.dodoadopter.dto.DoDoReplyMsgDTO;
import cyou.wssy001.dodoadopter.enums.DoDoReplyMsgTemplateEnum;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * @Description: DoDo获取用户ID事件处理器
 * @Author: Tyler
 * @Date: 2023/3/16 10:36
 * @Version: 1.0
 */
@Service
public class GetUserIdDoDoEventHandler implements BaseHandler {

    @Override
    public PlatformEnum getPlatform() {
        return PlatformEnum.DODO;
    }

    @Override
    public Set<String> getKeys() {
        return Set.of("/获取ID", "/获取id");
    }

    @Override
    public EventEnum getEventType() {
        return EventEnum.FRIEND;
    }

    @Override
    public String getDescription() {
        return "获取您的DoDo ID，可用于添加DoDo管理员列表";
    }

    @Override
    public BaseReplyMsgDTO consume(BaseEvent baseEvent, BasePlatformEventDTO basePlatformEventDTO) {
        if (baseEvent instanceof BasePrivateEvent && basePlatformEventDTO instanceof DoDoEventDTO dodoEventDTO) {
            String eventKey = baseEvent.getEventKey();
            DoDoEventDTO.EventBody dodoEventDTOData = dodoEventDTO.getData();
            JSONObject eventBody = dodoEventDTOData.getEventBody();
            String islandSourceId = eventBody.getString("islandSourceId");
            String dodoSourceId = eventBody.getString("dodoSourceId");
            String nickName = eventBody.getJSONObject("personal")
                    .getString("nickName");

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("        ");
            stringBuilder.append(nickName)
                    .append("    您好：\\n您的DoDo ID为：")
                    .append(dodoSourceId);

            String format = String.format(DoDoReplyMsgTemplateEnum.TEXT_MSG_TEMPLATE.getMsg(), stringBuilder);
            String replyMsg = String.format(DoDoReplyMsgTemplateEnum.PRIVATE_TEXT_MSG.getMsg(), islandSourceId, dodoSourceId, format);
            return new DoDoReplyMsgDTO()
                    .setApiEndPoint("/api/v2/personal/message/send")
                    .setEventKey(eventKey)
                    .setMsg(replyMsg);
        }
        return null;
    }
}
