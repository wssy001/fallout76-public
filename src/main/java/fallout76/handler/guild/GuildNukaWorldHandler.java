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
public class GuildNukaWorldHandler implements QQGuildChannelBaseHandler {
    private static final Logger LOG = Logger.getLogger(GuildNukaWorldHandler.class);

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
                        "type": "text",
                        "data": {
                            "text": "请收好以下指南，核子世界环游见！\\n"
                        }
                    },
                    {
                        "type": "text",
                        "data": {
                            "text": "%s\\n"
                        }
                    },
                    {
                        "type": "image",
                        "data": {
                            "file": "%s"
                        }
                    },
                    {
                        "type": "text",
                        "data": {
                            "text": "\\n"
                        }
                    },
                    {
                        "type": "text",
                        "data": {
                            "text": "%s\\n"
                        }
                    },
                    {
                        "type": "image",
                        "data": {
                            "file": "%s"
                        }
                    },
                    {
                        "type": "text",
                        "data": {
                            "text": "\\n"
                        }
                    },
                    {
                        "type": "text",
                        "data": {
                            "text": "%s\\n"
                        }
                    },
                    {
                        "type": "image",
                        "data": {
                            "file": "%s"
                        }
                    },
                    {
                        "type": "text",
                        "data": {
                            "text": "\\n"
                        }
                    },
                    {
                        "type": "text",
                        "data": {
                            "text": "%s\\n"
                        }
                    },
                    {
                        "type": "image",
                        "data": {
                            "file": "%s"
                        }
                    },
                    {
                        "type": "text",
                        "data": {
                            "text": "\\n"
                        }
                    },
                    {
                        "type": "text",
                        "data": {
                            "text": "%s\\n"
                        }
                    },
                    {
                        "type": "image",
                        "data": {
                            "file": "%s"
                        }
                    },
                    {
                        "type": "text",
                        "data": {
                            "text": "\\n"
                        }
                    }
                ]
            }
            """;

    @Override
    public List<String> getKeys() {
        return List.of("/核子世界","/核子世界环游");
    }

    @Override
    public String description() {
        return "获取核子世界环游攻略";
    }

    @Override
    public void execute(QQMessageEvent qqMessageEvent, String key) {
        LOG.infof("正在处理 QQ Guild：%s 指令", key);

        String guildId = qqMessageEvent.getGuildId();
        String channelId = qqMessageEvent.getChannelId();
        if (StrUtil.hasBlank(guildId, channelId)) {
            LOG.errorf("处理 QQ Guild：%s 指令失败，原因：%s", key, "guildId或channelId无效");
            return;
        }

        String nukaWorldGuide1 = photoService.getPhoto("nukaWorldGuide1");
        String nukaWorldGuide2 = photoService.getPhoto("nukaWorldGuide2");
        String nukaWorldGuide3 = photoService.getPhoto("nukaWorldGuide3");
        String nukaWorldGuide4 = photoService.getPhoto("nukaWorldGuide4");
        String nukaWorldMap = photoService.getPhoto("nukaWorldMap");
        if (StrUtil.hasBlank(nukaWorldGuide1, nukaWorldGuide2, nukaWorldGuide3, nukaWorldGuide4, nukaWorldMap)) {
            String msgBody = String.format(errorMsgTemplate, guildId, channelId, "无法获取周报，请联系管理员");
            replyService.sendQQGuildChannelMessage(msgBody, key);
            return;
        }
        String msgBody = String.format(MSG_TEMPLATE, guildId, channelId, "图1：核子世界环游公共事件奖励汇总图", nukaWorldGuide1, "图2：核子世界环游通用奖励汇总图", nukaWorldGuide2, "图3：核子电玩兑换奖励汇总图", nukaWorldGuide3, "图4：核子世界环游新增食物汇总图", nukaWorldGuide4, "图5：核子世界环游乐园导览指南", nukaWorldMap);
        replyService.sendQQGuildChannelMessage(msgBody, key);
        LOG.infof("指令 QQ Guild：%s 处理完毕", key);
    }

}
