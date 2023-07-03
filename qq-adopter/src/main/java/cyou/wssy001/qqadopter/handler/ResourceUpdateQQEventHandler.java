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
public class ResourceUpdateQQEventHandler implements BaseHandler {
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
        return Set.of("/资源更新");
    }

    @Override
    public String getDescription() {
        return "动态更新资源链接";
    }

    @Override
    public BaseReplyMsgDTO consume(BaseEvent baseEvent, BasePlatformEventDTO basePlatformEventDTO) {
        if (baseEvent instanceof BaseAdminEvent && basePlatformEventDTO instanceof QQEventDTO qqEventDTO) {
            Long userId = qqEventDTO.getUserId();
            String[] commandArgs = qqEventDTO.getRawMessage()
                    .split("\\s+");

            String format;
            if (commandArgs.length != 3) {
                log.error("******ResourceUpdateQQEventHandler.consume：资源更新失败，参数异常");
                format = String.format(QQReplyMsgTemplateEnum.TEXT_MSG_TEMPLATE.getMsg(), "参数异常，请检查");
            } else {
                String url = ReUtil.getGroup0(Validator.URL_HTTP.pattern(), commandArgs[2]);
                boolean updated = false;
                Map<String, String> map = new HashMap<>();
                if (StrUtil.isNotBlank(url)) {
                    map.put("1", url);
                    PhotoInfo photoInfo = new PhotoInfo()
                            .setPlatform(PlatformEnum.QQ)
                            .setKey(commandArgs[1])
                            .setUrlMap(map);
                    updated = photoService.updatePhotoUrl(photoInfo);
                    PhotoInfo photoInfo2 = BeanUtil.copyProperties(photoInfo, PhotoInfo.class);
                    photoInfo2.setPlatform(PlatformEnum.QQ_GUILD);
                    updated = updated && photoService.updatePhotoUrl(photoInfo2);
                }

                if (updated && photoService.storePhotoCache()) {
                    format = String.format(QQReplyMsgTemplateEnum.TEXT_MSG_TEMPLATE.getMsg(), "资源更新成功");
                } else {
                    log.error("******WeeklyOffersUpdateQQEventHandler.consume：原子商店特惠预览图更新失败");
                    format = String.format(QQReplyMsgTemplateEnum.TEXT_MSG_TEMPLATE.getMsg(), "资源更新失败");
                }
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
