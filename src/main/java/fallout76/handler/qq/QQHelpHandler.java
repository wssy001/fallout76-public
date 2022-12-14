package fallout76.handler.qq;

import cn.hutool.core.util.StrUtil;
import fallout76.config.RobotConfig;
import fallout76.controller.QQController;
import fallout76.entity.message.QQMessageEvent;
import fallout76.service.ReplyService;
import io.quarkus.runtime.StartupEvent;
import org.jboss.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class QQHelpHandler implements QQBaseGroupHandler {

    private static final Logger LOG = Logger.getLogger(QQHelpHandler.class);

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
        LOG.infof("正在初始化 %s", "GuildHelpHandler");
        if (qqBaseGroupHandlers.isUnsatisfied()) {
            LOG.errorf("初始化 %s 失败，%s 为空", "publicCommandJson", "qqBaseGroupHandlers");
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
        LOG.infof("初始化 %s 完成", "publicCommandJson");
    }

    public void initAdminCommandJson(@Observes StartupEvent event) {
        LOG.infof("正在初始化 %s", "GuildHelpHandler");
        if (qqBasePrivateHandlers.isUnsatisfied()) {
            LOG.errorf("初始化 %s 失败，%s 为空", "adminCommandJson", "qqBasePrivateHandlers");
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
        LOG.infof("初始化 %s 完成", "adminCommandJson");
    }

    @Override
    public void execute(QQMessageEvent qqMessageEvent, String key) {
        LOG.infof("正在处理 QQ： %s 指令", key);

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
        LOG.infof("处理 QQ： %s 指令完毕", key);
    }

}
