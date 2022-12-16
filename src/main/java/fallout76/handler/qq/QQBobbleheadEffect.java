package fallout76.handler.qq;

import fallout76.entity.message.QQMessageEvent;
import fallout76.service.PhotoService;
import fallout76.service.ReplyService;
import lombok.extern.slf4j.Slf4j;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Slf4j
@Singleton
public class QQBobbleheadEffect implements QQBaseGroupHandler {

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
        log.info("******QQBobbleheadEffect.execute：正在处理 QQ： {} 指令", key);
        long groupId = qqMessageEvent.getGroupId();
        String weeklyNews = photoService.getPhoto("bobbleheadEffects");
        if (groupId == 0) {
            log.error("******QQBobbleheadEffect.execute：处理 {} 指令失败，原因：{}", key, "groupId无效");
            return;
        }

        if (weeklyNews == null) {
            String msgBody = String.format(errorMsgTemplate, groupId, "无法获取周报，请联系管理员");
            replyService.sendQQGroupMessage(msgBody, key);
            return;
        }
        String msgBody = String.format(MSG_TEMPLATE, groupId, weeklyNews);
        replyService.sendQQGroupMessage(msgBody, key);
        log.info("******QQBobbleheadEffect.execute：处理 QQ： {} 指令完毕", key);
    }

}
