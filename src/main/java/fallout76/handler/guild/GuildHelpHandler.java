package fallout76.handler.guild;

import fallout76.controller.QQController;
import fallout76.entity.message.QQMessageEvent;
import fallout76.service.ReplyService;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class GuildHelpHandler implements QQGuildChannelBaseHandler {

    private static final Logger LOG = Logger.getLogger(QQController.class);

    @Inject
    ReplyService replyService;

    @Override
    public List<String> getKeys() {
        return List.of("/帮助", "/help");
    }

    @Override
    public String description() {
        return "获取当前环境下所有可用指令";
    }

    private static final String MSG_TEMPLATE = """
            {
                "guild_id": %s,
                "channel_id": %s,
                "message": [
                    {
                        "type": "text",
                        "data": {
                            "text": "/周报\\t获取麦片哥最新的周报\\n/帮助\\t/help\\t获取当前环境下所有可用指令\\n"
                        }
                    }
                ]
            }
            """;


    @Override
    public void execute(QQMessageEvent qqMessageEvent, String key) {
        LOG.infof("正在处理 QQ Guild： %s 指令", key);

        String guildId = qqMessageEvent.getGuildId();
        String channelId = qqMessageEvent.getChannelId();
        String msgBody = String.format(MSG_TEMPLATE, guildId, channelId);
        replyService.sendQQGuildChannelMessage(msgBody, key);

        LOG.infof("处理 QQ Guild： %s 指令完毕", key);
    }

}
