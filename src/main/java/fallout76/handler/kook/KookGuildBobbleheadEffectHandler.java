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
public class KookGuildBobbleheadEffectHandler implements KookGuildHandler {

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

    private static final String BOBBLEHEAD_EFFECT_CARD = """
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
                      "content": "娃娃效果"
                    }
                  },
                  {
                    "type": "divider"
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
        return List.of("/娃娃效果");
    }

    @Override
    public String description() {
        return "获取游戏中娃娃消耗品的效果";
    }


    @Override
    public void execute(KookEvent kookEvent, String key) {
        log.info("******KookGuildBobbleheadEffectHandler.execute：正在处理 Kook Guild： {} 指令", key);

        String targetId = kookEvent.getTargetId();
        if (StrUtil.hasBlank(targetId)) {
            log.error("******KookGuildBobbleheadEffectHandler.execute：处理 Kook Guild：{} 指令失败，原因：{}", key, "targetId无效");
            return;
        }

        try {
            String bobbleheadEffects1 = photoService.getPhoto("bobbleheadEffects1");
            String bobbleheadEffects2 = photoService.getPhoto("bobbleheadEffects2");
            if (StrUtil.hasBlank(bobbleheadEffects1, bobbleheadEffects2)) {
                log.error("******KookGuildBobbleheadEffectHandler.execute：娃娃效果图片获取失败");
                String format = String.format(errorMsgCard, "娃娃效果图片获取失败，请联系管理员");
                String body = String.format(MSG_TEMPLATE, targetId, StringEscapeUtils.escapeJava(format));
                replyService.sendKookGuildChannelMessage(body, key);
                return;
            }

            String format = String.format(BOBBLEHEAD_EFFECT_CARD, bobbleheadEffects1, bobbleheadEffects1, bobbleheadEffects2, bobbleheadEffects2);
            String body = String.format(MSG_TEMPLATE, targetId, StringEscapeUtils.escapeJava(format));
            replyService.sendKookGuildChannelMessage(body, key);
        } catch (Exception e) {
            log.error("******KookGuildBobbleheadEffectHandler.execute：回复 Kook Guild：{} 指令失败，原因：{}", key, e.getMessage());
        }

        log.info("******KookGuildBobbleheadEffectHandler.execute：处理 Kook Guild：{} 指令完毕", key);
    }
}
