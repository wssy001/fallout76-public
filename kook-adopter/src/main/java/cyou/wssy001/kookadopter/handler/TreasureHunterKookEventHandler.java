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
public class TreasureHunterKookEventHandler implements BaseHandler {
    private final PhotoService photoService;


    @Override
    public PlatformEnum getPlatform() {
        return PlatformEnum.KOOK;
    }

    @Override
    public EventEnum getEventType() {
        return EventEnum.GROUP;
    }

    @Override
    public Set<String> getKeys() {
        return Set.of("/鼹鼠","/鼹鼠旷工寻宝");
    }

    @Override
    public String getDescription() {
        return "获取鼹鼠旷工寻宝猎人奖励清单图";
    }

    @Override
    public BaseReplyMsgDTO consume(BaseEvent baseEvent, BasePlatformEventDTO basePlatformEventDTO) {
        if (basePlatformEventDTO instanceof KookEventDTO kookEventDTO) {
            String targetId = kookEventDTO.getTargetId();
            Map<String, String> treasureHunterPicUrls = photoService.getPhotoUrls("treasureHunter", this.getPlatform());
            String format;
            if (CollUtil.isEmpty(treasureHunterPicUrls)) {
                log.error("******TreasureHunterKookEventHandler.consume：鼹鼠旷工寻宝猎人奖励清单图获取失败");
                format = String.format(KookReplyMsgTemplateEnum.ERROR_MSG_CARD.getMsg(), "鼹鼠旷工寻宝猎人奖励清单图获取失败，请联系管理员");
            } else {
                format = String.format(KookReplyMsgTemplateEnum.TREASURE_HUNTER_OFFERS_CARD.getMsg(), treasureHunterPicUrls.get("1"));
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
