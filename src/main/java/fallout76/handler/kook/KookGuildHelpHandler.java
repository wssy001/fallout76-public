package fallout76.handler.kook;

import cn.hutool.core.util.StrUtil;
import fallout76.entity.message.KookEvent;
import fallout76.service.ReplyService;
import io.quarkus.runtime.StartupEvent;
import io.vertx.ext.web.handler.sockjs.impl.StringEscapeUtils;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Slf4j
@Singleton
public class KookGuildHelpHandler implements KookGuildHandler {

    @Inject
    ReplyService replyService;
    @Inject
    Instance<KookGuildHandler> kookGuildHandlers;

    private static final String MSG_TEMPLATE = """
            {
                "type": 10,
                "target_id": %s,
                "content": "%s"
            }
            """;

    private static String publicCommandJson = """
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
                       "content": "指令列表"
                     }
                   },
                   {
                     "type": "divider"
                   },
                   {
                     "type": "section",
                     "text": {
                       "type": "kmarkdown",
                       "content": "%s"
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
        return List.of("/help", "/帮助");
    }

    @Override
    public String description() {
        return "获取当前环境下所有可用指令";
    }

    public void initPublicCommandJson(@Observes StartupEvent event) {
        log.info("******KookGuildHelpHandler.initPublicCommandJson：正在初始化");

        if (kookGuildHandlers.isUnsatisfied()) {
            log.error("******KookGuildHelpHandler.initPublicCommandJson：初始化 {} 失败，{} 为空", "publicCommandJson", "kookGuildHandlers");
            publicCommandJson = String.format(publicCommandJson, "暂未找到相关指令");
            return;
        }

        StringBuffer stringBuffer = new StringBuffer();
        kookGuildHandlers.forEach(handler -> {
            stringBuffer.append("`");
            handler.getKeys()
                    .forEach(key -> stringBuffer.append(key)
                            .append("\\t"));
            stringBuffer.append("`")
                    .append("\\t\\t")
                    .append(handler.description())
                    .append("\\n");
        });
        publicCommandJson = String.format(publicCommandJson, stringBuffer);

        log.info("******KookGuildHelpHandler.initPublicCommandJson：初始化完成");
    }


    @Override
    public void execute(KookEvent kookEvent, String key) {
        log.info("******KookGuildHelpHandler.execute：正在处理 Kook Guild： {} 指令", key);

        String targetId = kookEvent.getTargetId();
        if (StrUtil.hasBlank(targetId)) {
            log.error("******KookGuildHelpHandler.execute：处理 Kook Guild：{} 指令失败，原因：{}", key, "targetId无效");
            return;
        }

        try {
            String body = String.format(MSG_TEMPLATE, targetId, StringEscapeUtils.escapeJava(publicCommandJson));
            replyService.sendKookGuildChannelMessage(body, key);
        } catch (Exception e) {
            log.error("******KookGuildHelpHandler.execute：回复 Kook Guild：{} 指令失败，原因：{}", key, e.getMessage());
        }

        log.info("******KookGuildHelpHandler.execute：处理 Kook Guild：{} 指令完毕", key);
    }
}
