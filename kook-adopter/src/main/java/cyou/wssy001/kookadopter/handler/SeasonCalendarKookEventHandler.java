package cyou.wssy001.kookadopter.handler;

import cn.hutool.core.collection.CollUtil;
import cyou.wssy001.common.dto.BasePlatformEventDTO;
import cyou.wssy001.common.dto.BaseReplyMsgDTO;
import cyou.wssy001.common.entity.BaseEvent;
import cyou.wssy001.common.enums.EventEnum;
import cyou.wssy001.common.enums.PlatformEnum;
import cyou.wssy001.common.handler.BaseHandler;
import cyou.wssy001.common.service.PhotoService;
import cyou.wssy001.kookadopter.dto.KookEventDTO;
import cyou.wssy001.kookadopter.dto.KookReplyMsgDTO;
import cyou.wssy001.kookadopter.enums.KookReplyMsgTemplateEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StringEscapeUtils;
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
public class SeasonCalendarKookEventHandler implements BaseHandler {
    private final PhotoService photoService;


    @Override
    public PlatformEnum platform() {
        return PlatformEnum.KOOK;
    }

    @Override
    public EventEnum eventType() {
        return EventEnum.GROUP;
    }

    @Override
    public Set<String> getKeys() {
        return Set.of("/社区日历", "/社区日程", "/赛季日程");
    }

    @Override
    public String description() {
        return "获取社区日程表";
    }

    @Override
    public BaseReplyMsgDTO consume(BaseEvent baseEvent, BasePlatformEventDTO basePlatformEventDTO) {
        if (basePlatformEventDTO instanceof KookEventDTO kookEventDTO) {
            String targetId = kookEventDTO.getTargetId();
            Map<String, String> seasonCalendarPicUrls = photoService.getPhotoUrls("seasonCalendar", PlatformEnum.KOOK);
            String replyMsg;
            if (CollUtil.isEmpty(seasonCalendarPicUrls)) {
                log.error("******SeasonCalendarKookEventHandler.consume：社区日程表图片获取失败");
                String format = String.format(KookReplyMsgTemplateEnum.ERROR_MSG_CARD.getMsg(), "社区日程表图片获取失败，请联系管理员");
                replyMsg = String.format(KookReplyMsgTemplateEnum.ERROR_MSG.getMsg(), targetId, StringEscapeUtils.escapeJava(format));
            } else {
                String format = String.format(KookReplyMsgTemplateEnum.SEASON_CALENDAR_CARD.getMsg(), seasonCalendarPicUrls.get("1"));
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
