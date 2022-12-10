package fallout76.handler.qq;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import fallout76.config.RobotConfig;
import fallout76.controller.QQController;
import fallout76.entity.message.QQMessageEvent;
import fallout76.service.ReplyService;
import io.quarkus.runtime.Startup;
import io.quarkus.runtime.StartupEvent;
import org.jboss.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
public class HelpHandler implements QQBaseGroupHandler {

    private static final Logger LOG = Logger.getLogger(QQController.class);

    @Inject
    RobotConfig robotConfig;
    @Inject
    ReplyService replyService;

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

    private String publicCommandJson, adminCommandJson;

    @PostConstruct
    public void init() {
        publicCommandJson = """
                [
                    {
                        "type": "text",
                        "data": {
                            "text": "/周报\\t获取麦片哥最新的周报\\n/帮助\\t/help\\t获取当前环境下所有可用指令\\n"
                        }
                    }
                ]
                """;
        adminCommandJson = """
                [
                    {
                        "type": "text",
                        "data": {
                            "text": "/周报\\t获取麦片哥最新的周报\\n/帮助\\t/help\\t获取当前环境下所有可用指令\\n"
                        }
                    }
                ]
                """;
    }

    @Override
    public void execute(QQMessageEvent qqMessageEvent, String key) {
        LOG.infof("正在处理 %s 指令", key);

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
        LOG.infof("处理 %s 指令完毕", key);
    }

}
