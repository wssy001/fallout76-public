package fallout76.handler.kook;

import cn.hutool.core.util.StrUtil;
import fallout76.entity.NukaCode;
import fallout76.entity.message.KookEvent;
import fallout76.service.NukaCodeService;
import fallout76.service.ReplyService;
import io.vertx.ext.web.handler.sockjs.impl.StringEscapeUtils;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Slf4j
@Singleton
public class KookGuildNukaCodeHandler implements KookGuildHandler {

    @Inject
    ReplyService replyService;
    @Inject
    NukaCodeService nukaCodeService;

    private static final String MSG_TEMPLATE = """
            {
                "type": 10,
                "target_id": %s,
                "content": "%s"
            }
            """;

    private static final String NUKA_CODE_CARD = """
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
                      "content": "核弹密码"
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
                        "src": "https://s1.ax1x.com/2022/11/22/z1p5Jf.png"
                      }
                    ]
                  },
                  {
                    "type": "section",
                    "text": {
                      "type": "kmarkdown",
                      "content": "\\tA点：\\t\\t%s\\n\\tB点：\\t\\t%s\\n\\tC点：\\t\\t%s\\n"
                    }
                  },
                  {
                    "type": "countdown",
                    "mode": "day",
                    "endTime": %d
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
        return List.of("/核弹密码");
    }

    @Override
    public String description() {
        return "获取目前最新的核弹密码";
    }


    @Override
    public void execute(KookEvent kookEvent, String key) {
        log.info("******KookGuildNukaCodeHandler.execute：正在处理 Kook Guild： {} 指令", key);

        String targetId = kookEvent.getTargetId();
        if (StrUtil.hasBlank(targetId)) {
            log.error("******KookGuildNukaCodeHandler.execute：处理 Kook Guild：{} 指令失败，原因：{}", key, "targetId无效");
            return;
        }

        try {
            NukaCode nukaCode = nukaCodeService.getNukaCode();
            if (nukaCode == null) {
                log.error("******KookGuildNukaCodeHandler.execute：nukaCode获取失败");
                String format = String.format(errorMsgCard, "nukaCode获取失败，请联系管理员");
                String body = String.format(MSG_TEMPLATE, targetId, StringEscapeUtils.escapeJava(format));
                replyService.sendKookGuildChannelMessage(body, key);
                return;
            }

            String format = String.format(NUKA_CODE_CARD, nukaCode.getAlpha(), nukaCode.getBravo(), nukaCode.getCharlie(), nukaCode.getExpireTime().getTime());
            String body = String.format(MSG_TEMPLATE, targetId, StringEscapeUtils.escapeJava(format));
            replyService.sendKookGuildChannelMessage(body, key);
        } catch (Exception e) {
            log.error("******KookGuildNukaCodeHandler.execute：回复 Kook Guild：{} 指令失败，原因：{}", key, e.getMessage());
        }

        log.info("******KookGuildNukaCodeHandler.execute：处理 Kook Guild：{} 指令完毕", key);
    }
}
