package fallout76.handler.qq;

import fallout76.config.RobotConfig;
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
public class QQHelpHandler implements QQBaseGroupHandler {

    @Inject
    RobotConfig robotConfig;
    @Inject
    ReplyService replyService;

    @Inject
    Instance<QQBaseGroupHandler> qqBaseGroupHandlers;

    private static final String MSG_TEMPLATE = """
            {
                "group_id": %s,
                "message": %s
            }
            """;

    private static String publicCommandJson = """
            [
                {
                    "type": "text",
                    "data": {
                        "text": "%s"
                    }
                }
            ]
            """;


    @Override
    public List<String> getKeys() {
        return List.of("/帮助", "/help");
    }

    @Override
    public String description() {
        return "获取当前环境下所有可用指令";
    }

    public void initPublicCommandJson(@Observes StartupEvent event) {
        log.info("******QQHelpHandler.initPublicCommandJson：正在初始化");
        if (qqBaseGroupHandlers.isUnsatisfied()) {
            log.error("******QQHelpHandler.initPublicCommandJson：初始化 {} 失败，{} 为空", "publicCommandJson", "qqBaseGroupHandlers");
            publicCommandJson = String.format(publicCommandJson, "暂未找到相关指令");
            return;
        }

        StringBuffer stringBuffer = new StringBuffer();
        qqBaseGroupHandlers.forEach(handler -> {
            handler.getKeys()
                    .forEach(key -> stringBuffer.append(key)
                            .append("\\t"));
            stringBuffer.append(handler.description())
                    .append("\\n");
        });
        publicCommandJson = String.format(publicCommandJson, stringBuffer);
        log.info("******QQHelpHandler.initPublicCommandJson：初始化完成");
    }

    @Override
    public void execute(QQMessageEvent qqMessageEvent, String key) {
        log.info("******QQHelpHandler.execute：正在处理 QQ： {} 指令", key);

        long groupId = qqMessageEvent.getGroupId();
        if (groupId == 0) {
            log.error("******QQHelpHandler.execute：处理 QQ：{} 指令失败，原因：{}", key, "groupId无效");
            return;
        }

        String msgBody = String.format(MSG_TEMPLATE, groupId, publicCommandJson);
        replyService.sendQQGroupMessage(msgBody, key);

        log.info("******QQHelpHandler.execute：处理 QQ： {} 指令完毕", key);
    }

}
