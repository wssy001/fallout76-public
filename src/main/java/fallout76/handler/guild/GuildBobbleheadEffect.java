package fallout76.handler.guild;

import cn.hutool.core.util.StrUtil;
import fallout76.entity.message.QQMessageEvent;
import fallout76.service.PhotoService;
import fallout76.service.ReplyService;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class GuildBobbleheadEffect implements QQGuildChannelBaseHandler {
    private static final Logger LOG = Logger.getLogger(GuildBobbleheadEffect.class);

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
                    }
                ]
            }
            """;

    @Override
    public List<String> getKeys() {
        return List.of("/娃娃效果");
    }

    @Override
    public String description() {
        return "获取游戏中娃娃消耗品的效果";
    }

    @Override
    public void execute(QQMessageEvent qqMessageEvent, String key) {
        LOG.infof("正在处理 QQ Guild：%s 指令", key);

        String guildId = qqMessageEvent.getGuildId();
        String channelId = qqMessageEvent.getChannelId();
        String weeklyNews = photoService.getPhoto("bobbleheadEffects");
        if (StrUtil.hasBlank(guildId, channelId)) {
            LOG.errorf("处理 QQ Guild：%s 指令失败，原因：%s", key, "guildId或channelId无效");
            return;
        }

        if (weeklyNews == null) {
            String msgBody = String.format(errorMsgTemplate, guildId, channelId, "无法获取周报，请联系管理员");
            replyService.sendQQGuildChannelMessage(msgBody, key);
            return;
        }
        String msgBody = String.format(MSG_TEMPLATE, guildId, channelId, weeklyNews);
        replyService.sendQQGuildChannelMessage(msgBody, key);
        LOG.infof("指令 QQ Guild：%s 处理完毕", key);
    }

}
