package fallout76.handler.guild;

import fallout76.entity.message.QQMessageEvent;
import fallout76.service.ReplyService;
import io.quarkus.runtime.StartupEvent;
import org.jboss.logging.Logger;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class GuildHelpHandler implements QQGuildChannelBaseHandler {
    private static final Logger LOG = Logger.getLogger(GuildHelpHandler.class);

    @Inject
    Instance<QQGuildChannelBaseHandler> qqGuildChannelBaseHandlers;

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

    private static String MSG_TEMPLATE = """
            {
                "guild_id": %s,
                "channel_id": %s,
                "message": [
                    {
                        "type": "text",
                        "data": {
                            "text": "%s"
                        }
                    }
                ]
            }
            """;

    public void init(@Observes StartupEvent event) {
        LOG.infof("正在初始化 %s", "GuildHelpHandler");
        if (qqGuildChannelBaseHandlers.isUnsatisfied()) {
            LOG.errorf("初始化 %s 失败，%s 为空", "GuildHelpHandler", "qqGuildChannelBaseHandlers");
            MSG_TEMPLATE = String.format(MSG_TEMPLATE, "%s", "%s", "暂未找到相应指令");
            return;
        }

        StringBuffer stringBuffer = new StringBuffer();
        qqGuildChannelBaseHandlers.forEach(handler -> {
            handler.getKeys()
                    .forEach(key -> stringBuffer.append(key)
                            .append("\\t"));
            stringBuffer.append(handler.description())
                    .append("\\n");
        });
        MSG_TEMPLATE = String.format(MSG_TEMPLATE, "%s", "%s", stringBuffer);
        LOG.infof("初始化 %s 完成", "GuildHelpHandler");
    }

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
