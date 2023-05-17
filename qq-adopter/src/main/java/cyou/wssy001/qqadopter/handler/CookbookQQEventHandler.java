package cyou.wssy001.qqadopter.handler;

import cn.hutool.core.collection.CollUtil;
import cyou.wssy001.common.dto.BasePlatformEventDTO;
import cyou.wssy001.common.dto.BaseReplyMsgDTO;
import cyou.wssy001.common.entity.BaseEvent;
import cyou.wssy001.common.enums.EventEnum;
import cyou.wssy001.common.enums.PlatformEnum;
import cyou.wssy001.common.handler.BaseHandler;
import cyou.wssy001.common.service.PhotoService;
import cyou.wssy001.qqadopter.dto.QQEventDTO;
import cyou.wssy001.qqadopter.dto.QQReplyMsgDTO;
import cyou.wssy001.qqadopter.enums.QQReplyMsgTemplateEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

/**
 * @Description:
 * @Author: Tyler
 * @Date: 2023/4/24 14:42
 * @Version: 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CookbookQQEventHandler implements BaseHandler {
    private final PhotoService photoService;


    @Override
    public PlatformEnum getPlatform() {
        return PlatformEnum.QQ;
    }

    @Override
    public EventEnum getEventType() {
        return EventEnum.GROUP;
    }

    @Override
    public Set<String> getKeys() {
        return Set.of("/食物", "/常见食物");
    }

    @Override
    public String getDescription() {
        return "获取常见食物图";
    }

    @Override
    public BaseReplyMsgDTO consume(BaseEvent baseEvent, BasePlatformEventDTO basePlatformEventDTO) {
        if (basePlatformEventDTO instanceof QQEventDTO qqEventDTO) {
            Long groupId = qqEventDTO.getGroupId();
            Map<String, String> cookbookPicUrls = photoService.getPhotoUrls("cookbook", this.getPlatform());
            String format;
            if (CollUtil.isEmpty(cookbookPicUrls)) {
                log.error("******CookbookQQEventHandler.consume：常见食物图图片获取失败");
                format = String.format(QQReplyMsgTemplateEnum.TEXT_MSG_TEMPLATE.getMsg(), "常见食物图图片获取失败，请联系管理员");
            } else {
                format = String.format(QQReplyMsgTemplateEnum.COOKBOOK_MSG_TEMPLATE.getMsg(), cookbookPicUrls.get("1"), cookbookPicUrls.get("2"));
            }

            String replyMsg = String.format(QQReplyMsgTemplateEnum.GROUP_TEXT_MSG.getMsg(), groupId, format);
            return new QQReplyMsgDTO()
                    .setApiEndPoint("/send_group_msg")
                    .setEventKey(baseEvent.getEventKey())
                    .setMsg(replyMsg);
        }
        return null;
    }
}
