package cyou.wssy001.qqadopter.handler;

import cn.hutool.core.collection.CollUtil;
import cyou.wssy001.common.dto.BasePlatformEventDTO;
import cyou.wssy001.common.dto.BaseReplyMsgDTO;
import cyou.wssy001.common.entity.BaseEvent;
import cyou.wssy001.common.enums.EventEnum;
import cyou.wssy001.common.enums.PlatformEnum;
import cyou.wssy001.common.handler.BaseHandler;
import cyou.wssy001.common.service.PhotoService;
import cyou.wssy001.qqadopter.dto.QQChannelEventDTO;
import cyou.wssy001.qqadopter.dto.QQReplyMsgDTO;
import cyou.wssy001.qqadopter.enums.QQReplyMsgTemplateEnum;
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
public class MothManQQGuildEventHandler implements BaseHandler {
    private final PhotoService photoService;


    @Override
    public PlatformEnum getPlatform() {
        return PlatformEnum.QQ_GUILD;
    }

    @Override
    public EventEnum getEventType() {
        return EventEnum.GROUP;
    }

    @Override
    public Set<String> getKeys() {
        return Set.of("/天蛾人", "/天蛾人事件","/天蛾人春分");
    }

    @Override
    public String getDescription() {
        return "获取天蛾人春分季节性攻略指南";
    }

    @Override
    public BaseReplyMsgDTO consume(BaseEvent baseEvent, BasePlatformEventDTO basePlatformEventDTO) {
        if (basePlatformEventDTO instanceof QQChannelEventDTO qqChannelEventDTO) {
            String guildId = qqChannelEventDTO.getGuildId();
            String channelId = qqChannelEventDTO.getChannelId();
            Map<String, String> mothManPicUrls = photoService.getPhotoUrls("moth-man", this.getPlatform());
            String replyMsg;
            if (CollUtil.isEmpty(mothManPicUrls)) {
                log.error("******MothManQQGuildEventHandler.consume：天蛾人春分季节性攻略指南图片获取失败");
                String format = String.format(QQReplyMsgTemplateEnum.TEXT_MSG_TEMPLATE.getMsg(), "天蛾人春分季节性攻略指南图片获取失败，请联系管理员");
                replyMsg = String.format(QQReplyMsgTemplateEnum.GUILD_TEXT_MSG.getMsg(), guildId, channelId, format);
            } else {
                String format = String.format(QQReplyMsgTemplateEnum.MOTH_MAN_MSG_TEMPLATE.getMsg(), mothManPicUrls.get("1"));
                replyMsg = String.format(QQReplyMsgTemplateEnum.GUILD_TEXT_MSG.getMsg(), guildId, channelId, format);
            }

            return new QQReplyMsgDTO()
                    .setApiEndPoint("/send_guild_channel_msg")
                    .setEventKey(baseEvent.getEventKey())
                    .setMsg(replyMsg);
        }
        return null;
    }
}
