package fallout76.handler.kook;

import cn.hutool.core.util.StrUtil;
import fallout76.entity.message.KookEvent;
import fallout76.service.PhotoService;
import fallout76.service.ReplyService;
import io.vertx.ext.web.handler.sockjs.impl.StringEscapeUtils;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Slf4j
@Singleton
public class KookGuildNukaWorldHandler implements KookGuildHandler {

    @Inject
    ReplyService replyService;
    @Inject
    PhotoService photoService;

    private static final String MSG_TEMPLATE = """
            {
                "type": 10,
                "target_id": %s,
                "content": "%s"
            }
            """;

    private static final String NUKA_WORLD_CARD = """
            [
              {
                "type": "card",
                "theme": "secondary",
                "size": "lg",
                "modules": [
                  {
                    "type": "header",
                    "text": {
                      "type": "plain-text",
                      "content": "核子世界环游"
                    }
                  },
                  {
                    "type": "divider"
                  },
                  {
                    "type": "section",
                    "text": {
                      "type": "kmarkdown",
                      "content": "**请收好以下指南，核子世界环游见！\\n备注：核子世界环游公共事件（地震活动、旋轮轮盘、爱的隧道及头号通缉犯）的稀有奖励包括公共事件独立设计图奖池、具名武器奖池\\n（图一）和通用奖池（图二），即玩家每次完成上述公共事件可以获得的稀有奖池包括：\\n- 当前事件的特殊稀有设计图 x1\\n- 当前事件的特殊具名传奇武器 x1\\n- 核子世界环游通用奖池稀有设计图 x1**\\n\\n"
                    }
                  },
                  {
                    "type": "section",
                    "text": {
                      "type": "kmarkdown",
                      "content": "**图一：核子世界环游公共事件奖励汇总图**"
                    }
                  },
                  {
                    "type": "container",
                    "elements": [
                      {
                        "type": "image",
                        "src": "%s"
                      }
                    ]
                  },
                  {
                    "type": "section",
                    "text": {
                      "type": "kmarkdown",
                      "content": "[点击查看大图](%s)\\n\\n"
                    }
                  },
                  {
                    "type": "section",
                    "text": {
                      "type": "kmarkdown",
                      "content": "**图二：核子世界环游通用奖励汇总图**"
                    }
                  },
                  {
                    "type": "container",
                    "elements": [
                      {
                        "type": "image",
                        "src": "%s"
                      }
                    ]
                  },
                  {
                    "type": "section",
                    "text": {
                      "type": "kmarkdown",
                      "content": "[点击查看大图](%s)\\n\\n"
                    }
                  },
                  {
                    "type": "section",
                    "text": {
                      "type": "kmarkdown",
                      "content": "**图三：核子电玩兑换奖励汇总图**"
                    }
                  },
                  {
                    "type": "container",
                    "elements": [
                      {
                        "type": "image",
                        "src": "%s"
                      }
                    ]
                  },
                  {
                    "type": "section",
                    "text": {
                      "type": "kmarkdown",
                      "content": "[点击查看大图](%s)\\n\\n"
                    }
                  },
                  {
                    "type": "section",
                    "text": {
                      "type": "kmarkdown",
                      "content": "**图四：核子世界环游新增食物汇总图**"
                    }
                  },
                  {
                    "type": "container",
                    "elements": [
                      {
                        "type": "image",
                        "src": "%s"
                      }
                    ]
                  },
                  {
                    "type": "section",
                    "text": {
                      "type": "kmarkdown",
                      "content": "[点击查看大图](%s)\\n\\n"
                    }
                  },
                  {
                    "type": "section",
                    "text": {
                      "type": "kmarkdown",
                      "content": "**图五：核子世界环游乐园导览指南**"
                    }
                  },
                  {
                    "type": "section",
                    "text": {
                      "type": "kmarkdown",
                      "content": "[点击查看大图](%s)\\n\\n"
                    }
                  },
                  {
                    "type": "container",
                    "elements": [
                      {
                        "type": "image",
                        "src": "%s"
                      }
                    ]
                  },
                  {
                    "type": "divider"
                  },
                  {
                    "type": "context",
                    "elements": [
                      {
                        "type": "image",
                        "src": "https://img.kookapp.cn/assets/2022-06/N6ymk3YYuC0sg0sg.png"
                      },
                      {
                        "type": "kmarkdown",
                        "content": "[辐射76小助手](https://www.kookapp.cn/app/oauth2/authorize?id=11214&permissions=268288&client_id=L1CBDfziwUZWykMC&redirect_uri=&scope=bot)"
                      }
                    ]
                  }
                ]
              }
            ]
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
    public void execute(KookEvent kookEvent, String key) {
        log.info("******KookGuildNukaWorldHandler.execute：正在处理 Kook Guild： {} 指令", key);

        String targetId = kookEvent.getTargetId();
        if (StrUtil.hasBlank(targetId)) {
            log.error("******KookGuildNukaWorldHandler.execute：处理 Kook Guild：{} 指令失败，原因：{}", key, "targetId无效");
            return;
        }

        try {
            String nukaWorldGuide1 = photoService.getPhoto("nukaWorldGuide1");
            String nukaWorldGuide2 = photoService.getPhoto("nukaWorldGuide2");
            String nukaWorldGuide3 = photoService.getPhoto("nukaWorldGuide3");
            String nukaWorldGuide4 = photoService.getPhoto("nukaWorldGuide4");
            String nukaWorldMap = photoService.getPhoto("nukaWorldMap");
            if (StrUtil.hasBlank(nukaWorldGuide1, nukaWorldGuide2, nukaWorldGuide3, nukaWorldGuide4, nukaWorldMap)) {
                log.error("******KookGuildNukaWorldHandler.execute：核子世界环游图片获取失败");
                String msgBody = String.format(errorMsgTemplate, targetId, "无法获取核子世界环游信息，请联系管理员");
                replyService.sendQQGuildChannelMessage(msgBody, key);
                return;
            }

            String format = String.format(NUKA_WORLD_CARD, nukaWorldGuide1, nukaWorldGuide1, nukaWorldGuide2, nukaWorldGuide2, nukaWorldGuide3, nukaWorldGuide3, nukaWorldGuide4, nukaWorldGuide4, nukaWorldMap, nukaWorldMap);
            String body = String.format(MSG_TEMPLATE, targetId, StringEscapeUtils.escapeJava(format));
            replyService.sendKookGuildChannelMessage(body, key);
        } catch (Exception e) {
            log.error("******KookGuildNukaWorldHandler.execute：回复 Kook Guild：{} 指令失败，原因：{}", key, e.getMessage());
        }

        log.info("******KookGuildNukaWorldHandler.execute：处理 Kook Guild：{} 指令完毕", key);
    }
}
