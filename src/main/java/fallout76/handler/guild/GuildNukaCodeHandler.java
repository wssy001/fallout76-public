package fallout76.handler.guild;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import fallout76.entity.NukaCode;
import fallout76.entity.message.QQMessageEvent;
import fallout76.service.NukaCodeService;
import fallout76.service.ReplyService;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Slf4j
@Singleton
public class GuildNukaCodeHandler implements QQGuildChannelBaseHandler {

    @Inject
    NukaCodeService nukaCodeService;

    @Inject
    ReplyService replyService;

    private static final String MSG_TEMPLATE = """
            {
                "guild_id": %s,
                "channel_id": %s,
                "message": [
                    {
                        "type": "text",
                        "data": {
                            "text": "\\n%s\\t%s\\n%s\\t%s\\n%s\\t%s\\n\\n过期时间：%s（北京时间）"
                        }
                    }
                ]
            }
            """;

    @Override
    public List<String> getKeys() {
        return List.of("/核弹密码");
    }

    @Override
    public String description() {
        return "获取目前最新的核弹密码";
    }

    @Override
    public void execute(QQMessageEvent qqMessageEvent, String key) {
        log.info("******GuildNukaCodeHandler.execute：正在处理 QQ Guild： {} 指令", key);

        String guildId = qqMessageEvent.getGuildId();
        String channelId = qqMessageEvent.getChannelId();
        if (StrUtil.hasBlank(guildId, channelId)) {
            log.error("******GuildNukaCodeHandler.execute：处理 QQ Guild：{} 指令失败，原因：{}", key, "guildId或channelId无效");
            return;
        }

        NukaCode nukaCode = nukaCodeService.getNukaCode();
        if (nukaCode == null) {
            String msgBody = String.format(errorMsgTemplate, guildId, channelId, "无法获取核弹密码，请联系管理员");
            replyService.sendQQGuildChannelMessage(msgBody, key);
            return;
        }

        String dateFormat = DateUtil.format(nukaCode.getExpireTime(), DatePattern.NORM_DATETIME_PATTERN);
        String msgBody = String.format(MSG_TEMPLATE, guildId, channelId, "A点：", nukaCode.getAlpha(), "B点：", nukaCode.getBravo(), "C点：", nukaCode.getCharlie(), dateFormat);
        replyService.sendQQGuildChannelMessage(msgBody, key);
        log.info("******GuildNukaCodeHandler.execute：处理 QQ Guild： {} 指令完毕", key);
    }

}
