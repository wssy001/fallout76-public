package fallout76.handler.qq;

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
public class QQNukaWorldHandler implements QQBaseGroupHandler {

    @Inject
    PhotoService photoService;

    @Inject
    ReplyService replyService;

    private static final String MSG_TEMPLATE = """
            {
                "group_id": %d,
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
        return List.of("/核子世界", "/核子世界环游");
    }

    @Override
    public String description() {
        return "获取核子世界环游攻略";
    }

    @Override
    public void execute(QQMessageEvent qqMessageEvent, String key) {
        log.info("******QQNukaWorldHandler.execute：正在处理 QQ： {} 指令", key);

        long groupId = qqMessageEvent.getGroupId();
        if (groupId == 0) {
            log.error("******QQNukaWorldHandler.execute：处理 {} 指令失败，原因：{}", key, "groupId无效");
            return;
        }

        String nukaWorldGuide1 = photoService.getPhoto("nukaWorldGuide1");
        String nukaWorldGuide2 = photoService.getPhoto("nukaWorldGuide2");
        String nukaWorldGuide3 = photoService.getPhoto("nukaWorldGuide3");
        String nukaWorldGuide4 = photoService.getPhoto("nukaWorldGuide4");
        String nukaWorldMap = photoService.getPhoto("nukaWorldMap");
        if (StrUtil.hasBlank(nukaWorldGuide1, nukaWorldGuide2, nukaWorldGuide3, nukaWorldGuide4, nukaWorldMap)) {
            log.error("******QQNukaWorldHandler.execute：核子世界环游图片获取失败");
            String msgBody = String.format(errorMsgTemplate, groupId, "无法获取核子世界环游信息，请联系管理员");
            replyService.sendQQGroupMessage(msgBody, key);
            return;
        }
        String msgBody = String.format(MSG_TEMPLATE, groupId, "图1：核子世界环游公共事件奖励汇总图", nukaWorldGuide1, "图2：核子世界环游通用奖励汇总图", nukaWorldGuide2, "图3：核子电玩兑换奖励汇总图", nukaWorldGuide3, "图4：核子世界环游新增食物汇总图", nukaWorldGuide4, "图5：核子世界环游乐园导览指南", nukaWorldMap);
        replyService.sendQQGroupMessage(msgBody, key);
        log.info("******QQNukaWorldHandler.execute：处理 QQ： {} 指令完毕", key);
    }

}
