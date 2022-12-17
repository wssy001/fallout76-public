package fallout76.handler.qq;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import fallout76.entity.NukaCode;
import fallout76.entity.message.QQMessageEvent;
import fallout76.service.NukaCodeService;
import fallout76.service.ReplyService;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Slf4j
@Singleton
public class QQNukaCodeHandler implements QQBaseGroupHandler {

    @Inject
    NukaCodeService nukaCodeService;

    @Inject
    ReplyService replyService;

    private static final String MSG_TEMPLATE = """
            {
                "group_id": %d,
                "message": [
                    {
                        "type": "text",
                        "data": {
                            "text": "\\n%s\\t%s\\n%s\\t%s\\n%s\\t%s\\n\\n过期时间：%s（北京时间）"
                        }
                    }
                ]
            }
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
    public void execute(QQMessageEvent qqMessageEvent, String key) {
        log.info("******QQNukaCodeHandler.execute：正在处理 QQ： {} 指令", key);
        long groupId = qqMessageEvent.getGroupId();
        if (groupId == 0) {
            log.error("******QQNukaCodeHandler.execute：处理 {} 指令失败，原因：{}", key, "groupId无效");
            return;
        }

        NukaCode nukaCode = nukaCodeService.getNukaCode();
        if (nukaCode == null) {
            String msgBody = String.format(errorMsgTemplate, groupId, "无法获取核弹密码，请联系管理员");
            replyService.sendQQGroupMessage(msgBody, key);
            return;
        }

        String dateFormat = DateUtil.format(nukaCode.getExpireTime(), DatePattern.NORM_DATETIME_PATTERN);
        String msgBody = String.format(MSG_TEMPLATE, groupId, "A点：", nukaCode.getAlpha(), "B点：", nukaCode.getBravo(), "C点：", nukaCode.getCharlie(), dateFormat);
        replyService.sendQQGroupMessage(msgBody, key);
        log.info("******QQNukaCodeHandler.execute：处理 QQ： {} 指令完毕", key);
    }

}
