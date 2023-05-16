package cyou.wssy001.qqadopter.handler;

import cn.hutool.core.util.StrUtil;
import cyou.wssy001.common.dto.BasePlatformEventDTO;
import cyou.wssy001.common.dto.BaseReplyMsgDTO;
import cyou.wssy001.common.entity.BaseEvent;
import cyou.wssy001.common.enums.EventEnum;
import cyou.wssy001.common.enums.PlatformEnum;
import cyou.wssy001.common.handler.BaseHandler;
import cyou.wssy001.common.handler.BaseHelpHandler;
import cyou.wssy001.common.service.PhotoService;
import cyou.wssy001.common.util.PathUtil;
import cyou.wssy001.qqadopter.dto.QQChannelEventDTO;
import cyou.wssy001.qqadopter.dto.QQReplyMsgDTO;
import cyou.wssy001.qqadopter.enums.QQReplyMsgTemplateEnum;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

/**
 * @Description: QQ频道帮助指令处理器
 * @Author: Tyler
 * @Date: 2023/4/14 23:57
 * @Version: 1.0
 */
@Component
public class GetHelpQQGuildEventHandler implements BaseHelpHandler {
    private final String msg;


    public GetHelpQQGuildEventHandler(List<BaseHandler> baseHandlers, PhotoService photoService) {
        StringBuilder stringBuilder = new StringBuilder();

        LinkedHashMap<Set<String>, String> commandMap = new LinkedHashMap<>();
        for (BaseHandler baseHandler : baseHandlers) {
            if (baseHandler.getKeys().contains("/help")) continue;
            if (!baseHandler.getPlatform().equals(PlatformEnum.QQ_GUILD)) continue;
            if (!baseHandler.getEventType().equals(EventEnum.GROUP)) continue;

            commandMap.put(baseHandler.getKeys(), baseHandler.getDescription());
            Iterator<String> iterator = baseHandler.getKeys()
                    .iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                stringBuilder.append(key);
                if (iterator.hasNext()) stringBuilder.append("\\t");
            }
            stringBuilder.append("\\t\\t")
                    .append(baseHandler.getDescription())
                    .append("\\n");
        }

        msg = stringBuilder.toString();
        photoService.createHelpPhoto("qq-guild-help.png", commandMap);
    }

    @Override
    public PlatformEnum getPlatform() {
        return PlatformEnum.QQ_GUILD;
    }

    @Override
    public EventEnum getEventType() {
        return EventEnum.GROUP;
    }

    @Override
    public BaseReplyMsgDTO consume(BaseEvent baseEvent, BasePlatformEventDTO basePlatformEventDTO) {
        if (basePlatformEventDTO instanceof QQChannelEventDTO qqChannelEventDTO) {
            String format;
            String guildId = qqChannelEventDTO.getGuildId();
            String channelId = qqChannelEventDTO.getChannelId();
            if (StrUtil.isBlank(msg)) {
                format = String.format(QQReplyMsgTemplateEnum.TEXT_MSG_TEMPLATE.getMsg(), "暂无帮助内容，请联系管理员添加");
            } else {
                File file = new File(PathUtil.getJarPath() + "/config/help/qq-guild-help.png");
                if (file.exists()) {
                    format = String.format(QQReplyMsgTemplateEnum.HELP_PHOTO_MSG_TEMPLATE.getMsg(), file.toURI());
                } else {
                    format = String.format(QQReplyMsgTemplateEnum.TEXT_MSG_TEMPLATE.getMsg(), msg);
                }
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
