package fallout76.handler.guild;

import cn.hutool.core.util.StrUtil;
import fallout76.entity.message.QQMessageEvent;
import fallout76.service.ReplyService;
import io.quarkus.runtime.StartupEvent;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Slf4j
@Singleton
public class GuildHelpHandler implements QQGuildChannelBaseHandler {

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
        log.info("******GuildHelpHandler.init：正在初始化");
        if (qqGuildChannelBaseHandlers.isUnsatisfied()) {
            log.info("******GuildHelpHandler.init：初始化 {} 失败，{} 为空", "GuildHelpHandler", "qqGuildChannelBaseHandlers");
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
        log.info("******GuildHelpHandler.init：初始化完成");
    }

    @Override
    public void execute(QQMessageEvent qqMessageEvent, String key) {
        log.info("******GuildHelpHandler.execute：正在处理 QQ Guild： {} 指令",key);

        String guildId = qqMessageEvent.getGuildId();
        String channelId = qqMessageEvent.getChannelId();
        if (StrUtil.hasBlank(guildId, channelId)) {
            log.error("******GuildHelpHandler.execute：处理 QQ Guild：{} 指令失败，原因：{}", key, "guildId或channelId无效");
            return;
        }

        String msgBody = String.format(MSG_TEMPLATE, guildId, channelId);
        replyService.sendQQGuildChannelMessage(msgBody, key);

        log.info("******GuildHelpHandler.execute：处理 QQ Guild： {} 指令完毕", key);
    }

}
