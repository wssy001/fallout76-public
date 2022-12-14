package fallout76.handler.qq;

import cn.hutool.core.util.StrUtil;
import fallout76.controller.QQController;
import fallout76.entity.message.QQMessageEvent;
import fallout76.service.PhotoService;
import fallout76.service.ReplyService;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class QQWeeklyNewsHandler implements QQBaseGroupHandler {
    private static final Logger LOG = Logger.getLogger(QQWeeklyNewsHandler.class);

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
                    },
                    {
                        "type": "text",
                        "data": {
                            "text": "\\n\\n"
                        }
                    },
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
        return List.of("/周报");
    }

    @Override
    public String description() {
        return "获取麦片哥最新的周报";
    }

    @Override
    public void execute(QQMessageEvent qqMessageEvent, String key) {
        LOG.infof("正在处理 QQ： %s 指令", key);
        long groupId = qqMessageEvent.getGroupId();

        if (groupId == 0) {
            LOG.errorf("处理 %s 指令失败，原因：%s", key, "groupId无效");
            return;
        }

        String weeklyNews1 = photoService.getPhoto("weeklyNews1");
        String weeklyNews2 = photoService.getPhoto("weeklyNews2");
        if (StrUtil.hasBlank(weeklyNews1, weeklyNews2)) {
            String msgBody = String.format(errorMsgTemplate, groupId, "无法获取周报，请联系管理员");
            replyService.sendQQGroupMessage(msgBody, key);
            return;
        }
        String msgBody = String.format(MSG_TEMPLATE, groupId, weeklyNews1, weeklyNews2);
        replyService.sendQQGroupMessage(msgBody, key);
        LOG.infof("处理 QQ： %s 指令完毕", key);
    }

}
