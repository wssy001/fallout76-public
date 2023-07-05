package cyou.wssy001.dodoadopter.handler;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson2.JSONObject;
import cyou.wssy001.common.dto.BasePlatformEventDTO;
import cyou.wssy001.common.dto.BaseReplyMsgDTO;
import cyou.wssy001.common.entity.BaseEvent;
import cyou.wssy001.common.enums.EventEnum;
import cyou.wssy001.common.enums.PlatformEnum;
import cyou.wssy001.common.handler.BaseHandler;
import cyou.wssy001.common.service.PhotoService;
import cyou.wssy001.dodoadopter.dto.DoDoEventDTO;
import cyou.wssy001.dodoadopter.dto.DoDoReplyMsgDTO;
import cyou.wssy001.dodoadopter.enums.DoDoReplyMsgTemplateEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class InvadersFromBeyondDoDoEventHandler implements BaseHandler {
    private final PhotoService photoService;


    @Override
    public PlatformEnum getPlatform() {
        return PlatformEnum.DODO;
    }

    @Override
    public EventEnum getEventType() {
        return EventEnum.GROUP;
    }

    @Override
    public Set<String> getKeys() {
        return Set.of("/外星人事件", "/外来入侵者");
    }

    @Override
    public String getDescription() {
        return "获取外来入侵者事件攻略指南图";
    }

    @Override
    public BaseReplyMsgDTO consume(BaseEvent baseEvent, BasePlatformEventDTO basePlatformEventDTO) {
        if (basePlatformEventDTO instanceof DoDoEventDTO dodoEventDTO) {
            DoDoEventDTO.EventBody dodoEventDTOData = dodoEventDTO.getData();
            JSONObject eventBody = dodoEventDTOData.getEventBody();
            String channelId = eventBody.getString("channelId");
            Map<String, String> invadersPicUrls = photoService.getPhotoUrls("invaders", this.getPlatform());
            String format;
            if (CollUtil.isEmpty(invadersPicUrls)) {
                log.error("******InvadersFromBeyondDoDoEventHandler.consume：外来入侵者事件攻略指南图获取失败");
                format = String.format(DoDoReplyMsgTemplateEnum.ERROR_MSG_TEMPLATE.getMsg(), "外来入侵者事件攻略指南图获取失败，请联系管理员");
            } else {
                format = String.format(DoDoReplyMsgTemplateEnum.INVADERS_CARD.getMsg(), invadersPicUrls.get("1"));
            }

            String replyMsg = String.format(DoDoReplyMsgTemplateEnum.CHANNEL_CARD_MSG.getMsg(), channelId, format);
            return new DoDoReplyMsgDTO()
                    .setApiEndPoint("/api/v2/channel/message/send")
                    .setEventKey(baseEvent.getEventKey())
                    .setMsg(replyMsg);
        }
        return null;
    }
}
