package cyou.wssy001.qqadopter.handler;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import cyou.wssy001.common.dto.BasePlatformEventDTO;
import cyou.wssy001.common.dto.BaseReplyMsgDTO;
import cyou.wssy001.common.entity.BaseAdminEvent;
import cyou.wssy001.common.entity.BaseEvent;
import cyou.wssy001.common.entity.PhotoInfo;
import cyou.wssy001.common.enums.EventEnum;
import cyou.wssy001.common.enums.PlatformEnum;
import cyou.wssy001.common.handler.BaseHandler;
import cyou.wssy001.common.service.PhotoService;
import cyou.wssy001.qqadopter.dto.QQEventDTO;
import cyou.wssy001.qqadopter.dto.QQReplyMsgDTO;
import cyou.wssy001.qqadopter.enums.QQReplyMsgTemplateEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class WeeklyOffersUpdateQQEventHandler implements BaseHandler {
    private final PhotoService photoService;


    @Override
    public PlatformEnum getPlatform() {
        return PlatformEnum.QQ;
    }

    @Override
    public EventEnum getEventType() {
        return EventEnum.ADMIN;
    }

    @Override
    public Set<String> getKeys() {
        return Set.of("/更新日替");
    }

    @Override
    public String getDescription() {
        return "更新原子商店特惠预览图";
    }

    @Override
    public BaseReplyMsgDTO consume(BaseEvent baseEvent, BasePlatformEventDTO basePlatformEventDTO) {
        if (baseEvent instanceof BaseAdminEvent && basePlatformEventDTO instanceof QQEventDTO qqEventDTO) {
            Long userId = qqEventDTO.getUserId();
            Map<String, String> map = new HashMap<>();
            String url = ReUtil.getGroup0(Validator.URL_HTTP.pattern(), qqEventDTO.getRawMessage());
            boolean updated = false;
            if (StrUtil.isNotBlank(url)) {
                map.put("1", url);
                PhotoInfo photoInfo = new PhotoInfo()
                        .setPlatform(PlatformEnum.QQ)
                        .setKey("weeklyOffers")
                        .setUrlMap(map);
                updated = photoService.updatePhotoUrl(photoInfo);
                PhotoInfo photoInfo2 = BeanUtil.copyProperties(photoInfo, PhotoInfo.class);
                photoInfo2.setPlatform(PlatformEnum.QQ_GUILD);
                updated = updated && photoService.updatePhotoUrl(photoInfo2);
            }

            String format;
            if (updated && photoService.storePhotoCache()) {
                format = String.format(QQReplyMsgTemplateEnum.TEXT_MSG_TEMPLATE.getMsg(), "原子商店特惠预览图更新成功");
            } else {
                log.error("******WeeklyOffersUpdateQQEventHandler.consume：原子商店特惠预览图更新失败");
                format = String.format(QQReplyMsgTemplateEnum.TEXT_MSG_TEMPLATE.getMsg(), "原子商店特惠预览图更新失败");
            }

            String replyMsg = String.format(QQReplyMsgTemplateEnum.PRIVATE_TEXT_MSG.getMsg(), userId, format);
            return new QQReplyMsgDTO()
                    .setApiEndPoint("/send_private_msg")
                    .setEventKey(baseEvent.getEventKey())
                    .setMsg(replyMsg);
        }
        return null;
    }
}
