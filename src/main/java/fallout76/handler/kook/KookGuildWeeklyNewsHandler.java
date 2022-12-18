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
public class KookGuildWeeklyNewsHandler implements KookGuildHandler {

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

    private static final String WEEKLY_NEWS_CARD = """
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
                      "content": "周报"
                    }
                  },
                  {
                    "type": "divider"
                  },
                  {
                    "type": "section",
                    "text": {
                      "type": "kmarkdown",
                      "content": "**图一：原子商店**"
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
                      "content": "[点击查看上图大图](%s)\\n\\n"
                    }
                  },
                  {
                    "type": "section",
                    "text": {
                      "type": "kmarkdown",
                      "content": "**图二：其他**"
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
                      "content": "[点击查看上图大图](%s)\\n"
                    }
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
        return List.of("/周报");
    }

    @Override
    public String description() {
        return "获取麦片哥最新的周报";
    }


    @Override
    public void execute(KookEvent kookEvent, String key) {
        log.info("******KookGuildWeeklyNewsHandler.execute：正在处理 Kook Guild： {} 指令", key);

        String targetId = kookEvent.getTargetId();
        if (StrUtil.hasBlank(targetId)) {
            log.error("******KookGuildWeeklyNewsHandler.execute：处理 Kook Guild：{} 指令失败，原因：{}", key, "targetId无效");
            return;
        }

        try {
            String weeklyNews1 = photoService.getPhoto("weeklyNews1");
            String weeklyNews2 = photoService.getPhoto("weeklyNews2");
            if (StrUtil.hasBlank(weeklyNews2, weeklyNews1)) {
                log.error("******KookGuildWeeklyNewsHandler.execute：周报图片获取失败");
                String msgBody = String.format(errorMsgTemplate, targetId, "无法获取周报信息，请联系管理员");
                replyService.sendQQGuildChannelMessage(msgBody, key);
                return;
            }

            String format = String.format(WEEKLY_NEWS_CARD, weeklyNews1, weeklyNews1, weeklyNews2, weeklyNews2);
            String body = String.format(MSG_TEMPLATE, targetId, StringEscapeUtils.escapeJava(format));
            replyService.sendKookGuildChannelMessage(body, key);
        } catch (Exception e) {
            log.error("******KookGuildWeeklyNewsHandler.execute：回复 Kook Guild：{} 指令失败，原因：{}", key, e.getMessage());
        }

        log.info("******KookGuildWeeklyNewsHandler.execute：处理 Kook Guild：{} 指令完毕", key);
    }
}
