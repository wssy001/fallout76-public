package fallout76.handler.qq;

import cn.hutool.core.util.StrUtil;
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
public class QQPrivateHelpHandler implements QQBasePrivateHandler {

    @Inject
    RobotConfig robotConfig;
    @Inject
    ReplyService replyService;

    @Inject
    Instance<QQBasePrivateHandler> qqBasePrivateHandlers;

    @Override
    public List<String> getKeys() {
        return List.of("/帮助", "/help");
    }

    @Override
    public String description() {
        return "获取当前环境下所有可用指令";
    }

    private static final String MSG_TEMPLATE = """
            {
                "user_id": %s,
                "message": %s
            }
            """;

    private static String adminCommandJson = """
            [
                {
                    "type": "text",
                    "data": {
                        "text": "%s"
                    }
                }
            ]
            """;

    public void initAdminCommandJson(@Observes StartupEvent event) {
        log.info("******QQHelpHandler.initAdminCommandJson：正在初始化");
        if (qqBasePrivateHandlers.isUnsatisfied()) {
            log.error("******QQHelpHandler.initAdminCommandJson：初始化 {} 失败，{} 为空", "adminCommandJson", "qqBasePrivateHandlers");
            adminCommandJson = String.format(adminCommandJson, "暂未找到管理员指令");
            return;
        }
        StringBuffer stringBuffer = new StringBuffer();
        qqBasePrivateHandlers.forEach(handler -> {
            handler.getKeys()
                    .forEach(key -> stringBuffer.append(key)
                            .append("\\t"));
            stringBuffer.append(handler.description())
                    .append("\\n");
        });
        adminCommandJson = String.format(adminCommandJson, stringBuffer);
        log.info("******QQHelpHandler.initAdminCommandJson：初始化完成");
    }

    @Override
    public void execute(QQMessageEvent qqMessageEvent, String key) {
        log.info("******QQHelpHandler.execute：正在处理 QQ Private： {} 指令", key);

        String userId = qqMessageEvent.getUserId();
        if (StrUtil.isBlank(userId)) {
            log.error("******QQPrivateHelpHandler.execute：处理 QQ Private：{} 指令失败，原因：{}", key, "userId无效");
            return;
        }

        boolean isPrivate = false, isAdmin = false;
        if (StrUtil.isNotBlank(userId)) {
            isPrivate = true;
            isAdmin = robotConfig.admins()
                    .contains(userId);
        }

        if (isPrivate && isAdmin) {
            String msgBody = String.format(MSG_TEMPLATE, userId, adminCommandJson);
            replyService.sendQQPrivateMessage(msgBody, key);
        }
        log.info("******QQHelpHandler.execute：处理 QQ Private： {} 指令完毕", key);
    }

}
