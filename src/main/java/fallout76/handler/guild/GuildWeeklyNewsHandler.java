package fallout76.handler.guild;

import cn.hutool.core.util.StrUtil;
import fallout76.entity.message.QQMessageEvent;
import fallout76.service.PhotoService;
import fallout76.service.ReplyService;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Slf4j
@Singleton
public class GuildWeeklyNewsHandler implements QQGuildChannelBaseHandler {

    @Inject
    PhotoService photoService;

    @Inject
    ReplyService replyService;

    private static final String MSG_TEMPLATE = """
            {
                "guild_id": %s,
                "channel_id": %s,
                "message": [
                    {
                        "type": "image",
                        "data": {
                            "file": "%s"
                        }
                    },
                    {
                        "type": "text",
                        "data": {
                            "text": "\\n\\n"
                        }
                    },
                    {
                        "type": "image",
                        "data": {
                            "file": "%s"
                        }
                    }
                ]
            }
            """;

    @Override
    public List<String> getKeys() {
        return List.of("/周报");
    }

    @Override
    public String description() {
        return "获取麦片哥最新的周报";
    }

    @Override
    public void execute(QQMessageEvent qqMessageEvent, String key) {
        log.info("******GuildWeeklyNewsHandler.execute：正在处理 QQ Guild：{} 指令", key);

        String guildId = qqMessageEvent.getGuildId();
        String channelId = qqMessageEvent.getChannelId();
        if (StrUtil.hasBlank(guildId, channelId)) {
            log.error("******GuildWeeklyNewsHandler.execute：处理 QQ Guild：{} 指令失败，原因：{}", key, "guildId或channelId无效");
            return;
        }

        String weeklyNews1 = photoService.getPhoto("weeklyNews1");
        String weeklyNews2 = photoService.getPhoto("weeklyNews2");
        if (StrUtil.hasBlank(weeklyNews1, weeklyNews2)) {
            String msgBody = String.format(errorMsgTemplate, guildId, channelId, "无法获取周报，请联系管理员");
            replyService.sendQQGuildChannelMessage(msgBody, key);
            return;
        }
        String msgBody = String.format(MSG_TEMPLATE, guildId, channelId, weeklyNews1, weeklyNews2);
        replyService.sendQQGuildChannelMessage(msgBody, key);
        log.info("******GuildWeeklyNewsHandler.execute：指令 QQ Guild：{} 处理完毕", key);
    }

}
