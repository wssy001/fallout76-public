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
public class CoastRewardsQQGuildEventHandler implements BaseHandler {
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
        return Set.of("/科斯塔的业务");
    }

    @Override
    public String getDescription() {
        return "获取科斯塔的业务系列任务流程和奖励图";
    }

    @Override
    public BaseReplyMsgDTO consume(BaseEvent baseEvent, BasePlatformEventDTO basePlatformEventDTO) {
        if (basePlatformEventDTO instanceof QQChannelEventDTO qqChannelEventDTO) {
            String guildId = qqChannelEventDTO.getGuildId();
            String channelId = qqChannelEventDTO.getChannelId();
            Map<String, String> coastRewardsPicUrls = photoService.getPhotoUrls("coastRewards", this.getPlatform());
            String format;
            if (CollUtil.isEmpty(coastRewardsPicUrls)) {
                log.error("******CoastRewardsQQGuildEventHandler.consume：科斯塔的业务系列任务流程和奖励图片获取失败");
                format = String.format(QQReplyMsgTemplateEnum.TEXT_MSG_TEMPLATE.getMsg(), "科斯塔的业务系列任务流程和奖励图片获取失败，请联系管理员");
            } else {
                format = String.format(QQReplyMsgTemplateEnum.COAST_REWARDS_MSG_TEMPLATE.getMsg(), coastRewardsPicUrls.get("1"), coastRewardsPicUrls.get("2"));
            }

            String replyMsg = String.format(QQReplyMsgTemplateEnum.GUILD_TEXT_MSG.getMsg(), guildId, channelId, format);
            return new QQReplyMsgDTO()
                    .setApiEndPoint("/send_guild_channel_msg")
                    .setEventKey(baseEvent.getEventKey())
                    .setMsg(replyMsg);
        }
        return null;
    }
}
