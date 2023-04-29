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
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

/**
 * @Description:
 * @Author: Tyler
 * @Date: 2023/4/24 14:42
 * @Version: 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BobbleHeadDoDoEventHandler implements BaseHandler {
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
        return Set.of("/娃娃", "/娃娃效果");
    }

    @Override
    public String getDescription() {
        return "获取娃娃效果图";
    }

    @Override
    public BaseReplyMsgDTO consume(BaseEvent baseEvent, BasePlatformEventDTO basePlatformEventDTO) {
        if (basePlatformEventDTO instanceof DoDoEventDTO dodoEventDTO) {
            DoDoEventDTO.EventBody dodoEventDTOData = dodoEventDTO.getData();
            JSONObject eventBody = dodoEventDTOData.getEventBody();
            String channelId = eventBody.getString("channelId");
            Map<String, String> bobbleHeadPicUrls = photoService.getPhotoUrls("bobbleHead", this.getPlatform());
            String replyMsg;
            if (CollUtil.isEmpty(bobbleHeadPicUrls)) {
                log.error("******BobbleHeadDoDoEventHandler.consume：娃娃效果图片获取失败");
                String format = String.format(DoDoReplyMsgTemplateEnum.ERROR_MSG_TEMPLATE.getMsg(), "娃娃效果图片获取失败，请联系管理员");
                replyMsg = String.format(DoDoReplyMsgTemplateEnum.CHANNEL_CARD_MSG.getMsg(), channelId, format);
            } else {
                String format = String.format(DoDoReplyMsgTemplateEnum.BOBBLE_HEAD_CARD.getMsg(), bobbleHeadPicUrls.get("1"));
                replyMsg = String.format(DoDoReplyMsgTemplateEnum.CHANNEL_CARD_MSG.getMsg(), channelId, format);
            }

            return new DoDoReplyMsgDTO()
                    .setApiEndPoint("/api/v2/channel/message/send")
                    .setEventKey(baseEvent.getEventKey())
                    .setMsg(replyMsg);
        }
        return null;
    }
}