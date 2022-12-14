package fallout76.handler.qq;

import fallout76.controller.QQController;
import fallout76.entity.message.QQMessageEvent;
import fallout76.service.PhotoService;
import fallout76.service.ReplyService;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class QQBobbleheadEffect implements QQBaseGroupHandler {
    private static final Logger LOG = Logger.getLogger(QQBobbleheadEffect.class);

    @Inject
    PhotoService photoService;

    @Inject
    ReplyService replyService;

    private static final String MSG_TEMPLATE = """
            {
                "group_id": %d,
                "message": [
                    {
                        "type": "image",
                        "data": {
                            "file": "%s"
                        }
                    }
                ]
            }
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
    public void execute(QQMessageEvent qqMessageEvent, String key) {
        LOG.infof("正在处理 QQ： %s 指令", key);
        long groupId = qqMessageEvent.getGroupId();
        String weeklyNews = photoService.getPhoto("bobbleheadEffects");
        if (groupId == 0) {
            LOG.errorf("处理 %s 指令失败，原因：%s", key, "groupId无效");
            return;
        }

        if (weeklyNews == null) {
            String msgBody = String.format(errorMsgTemplate, groupId, "无法获取周报，请联系管理员");
            replyService.sendQQGroupMessage(msgBody, key);
            return;
        }
        String msgBody = String.format(MSG_TEMPLATE, groupId, weeklyNews);
        replyService.sendQQGroupMessage(msgBody, key);
        LOG.infof("处理 QQ： %s 指令完毕", key);
    }

}
