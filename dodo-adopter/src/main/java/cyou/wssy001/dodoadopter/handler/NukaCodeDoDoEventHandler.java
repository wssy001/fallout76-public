package cyou.wssy001.dodoadopter.handler;

import com.alibaba.fastjson2.JSONObject;
import cyou.wssy001.common.dto.BasePlatformEventDTO;
import cyou.wssy001.common.dto.BaseReplyMsgDTO;
import cyou.wssy001.common.entity.BaseEvent;
import cyou.wssy001.common.entity.NukaCode;
import cyou.wssy001.common.enums.EventEnum;
import cyou.wssy001.common.enums.PlatformEnum;
import cyou.wssy001.common.handler.BaseHandler;
import cyou.wssy001.common.service.NukaCodeService;
import cyou.wssy001.dodoadopter.dto.DoDoEventDTO;
import cyou.wssy001.dodoadopter.dto.DoDoReplyMsgDTO;
import cyou.wssy001.dodoadopter.enums.DoDoReplyMsgTemplateEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.ZoneOffset;
import java.util.Set;

/**
 * @Description: DoDo核弹密码事件处理器
 * @Author: Tyler
 * @Date: 2023/3/16 10:36
 * @Version: 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NukaCodeDoDoEventHandler implements BaseHandler {
    private final NukaCodeService nukaCodeService;


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
        return Set.of("/核弹密码");
    }

    @Override
    public String getDescription() {
        return "获取目前最新的核弹密码";
    }

    @Override
    public BaseReplyMsgDTO consume(BaseEvent baseEvent, BasePlatformEventDTO basePlatformEventDTO) {
        if (basePlatformEventDTO instanceof DoDoEventDTO dodoEventDTO) {
            String eventKey = baseEvent.getEventKey();
            DoDoEventDTO.EventBody dodoEventDTOData = dodoEventDTO.getData();
            JSONObject eventBody = dodoEventDTOData.getEventBody();
            String channelId = eventBody.getString("channelId");
            NukaCode nukaCode = nukaCodeService.getNukaCode();
            String format;
            if (nukaCode == null) {
                log.error("******NukaCodeDoDoEventHandler.consume：nukaCode获取失败");
                format = String.format(DoDoReplyMsgTemplateEnum.ERROR_MSG_TEMPLATE.getMsg(), "nukaCode获取失败，请联系管理员", eventKey);
            } else {
                long epochMilli = nukaCode.getExpireTime()
                        .toInstant(ZoneOffset.ofHours(8))
                        .toEpochMilli();
                format = String.format(DoDoReplyMsgTemplateEnum.NUKA_CODE_CARD.getMsg(), nukaCode.getAlpha(), nukaCode.getBravo(), nukaCode.getCharlie(), epochMilli, eventKey);
            }

            String replyMsg = String.format(DoDoReplyMsgTemplateEnum.CHANNEL_CARD_MSG.getMsg(), channelId, format);
            return new DoDoReplyMsgDTO()
                    .setApiEndPoint("/api/v2/channel/message/send")
                    .setEventKey(eventKey)
                    .setMsg(replyMsg);
        }
        return null;
    }
}
