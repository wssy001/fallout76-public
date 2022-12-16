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
public class QQHelpHandler implements QQBaseGroupHandler {

    @Inject
    RobotConfig robotConfig;
    @Inject
    ReplyService replyService;

    @Inject
    Instance<QQBaseGroupHandler> qqBaseGroupHandlers;

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
                "%s": %s,
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

    public void initPublicCommandJson(@Observes StartupEvent event) {
        log.info("******QQHelpHandler.initPublicCommandJson：正在初始化");
        if (qqBaseGroupHandlers.isUnsatisfied()) {
            log.error("******QQHelpHandler.initPublicCommandJson：初始化 {} 失败，{} 为空", "publicCommandJson", "qqBaseGroupHandlers");
            publicCommandJson = String.format(adminCommandJson, "暂未找到相关指令");
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
        log.info("******QQHelpHandler.execute：正在处理 QQ： {} 指令", key);

        long groupId = qqMessageEvent.getGroupId();
        String userId = qqMessageEvent.getUserId();

        boolean isGroup = false, isPrivate = false, isAdmin = false;

        if (groupId != 0) {
            isGroup = true;
        } else if (StrUtil.isNotBlank(userId)) {
            isPrivate = true;
            isAdmin = robotConfig.admins()
                    .contains(userId);
        }

        if (isGroup) {
            String msgBody = String.format(MSG_TEMPLATE, "group_id", groupId, publicCommandJson);
            replyService.sendQQGroupMessage(msgBody, key);
        } else if (isPrivate && isAdmin) {
            String msgBody = String.format(MSG_TEMPLATE, "user_id", userId, adminCommandJson);
            replyService.sendQQPrivateMessage(msgBody, key);
        }
        log.info("******QQHelpHandler.execute：处理 QQ： {} 指令完毕", key);
    }

}
