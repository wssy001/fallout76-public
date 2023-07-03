package cyou.wssy001.kookadopter.handler;

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
import cyou.wssy001.common.service.ResourceUploadService;
import cyou.wssy001.kookadopter.dto.KookEventDTO;
import cyou.wssy001.kookadopter.dto.KookReplyMsgDTO;
import cyou.wssy001.kookadopter.enums.KookReplyMsgTemplateEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResourceUpdateKookEventHandler implements BaseHandler {
    private final PhotoService photoService;
    private final ResourceUploadService kookResourceUploadService;


    @Override
    public PlatformEnum getPlatform() {
        return PlatformEnum.KOOK;
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
        if (baseEvent instanceof BaseAdminEvent && basePlatformEventDTO instanceof KookEventDTO kookEventDTO) {
            String format;
            String content = kookEventDTO.getContent();
            String[] commandArgs = content.split("\\s+");
            if (commandArgs.length != 3) {
                log.error("******ResourceUpdateKookEventHandler.consume：资源更新失败，参数异常");
                format = "参数异常，请检查";
            } else {
                String url = ReUtil.getGroup0(Validator.URL_HTTP.pattern(), commandArgs[2]);
                boolean updated = false;
                url = kookResourceUploadService.upload(url);
                Map<String, String> map = new HashMap<>();
                if (StrUtil.isNotBlank(url)) {
                    map.put("1", url);
                    PhotoInfo photoInfo = new PhotoInfo()
                            .setPlatform(PlatformEnum.KOOK)
                            .setKey(commandArgs[1])
                            .setUrlMap(map);
                    updated = photoService.updatePhotoUrl(photoInfo);
                }

                if (updated && photoService.storePhotoCache()) {
                    format = "资源更新成功";
                } else {
                    log.error("******ResourceUpdateKookEventHandler.consume：资源更新失败");
                    format = "资源更新失败";
                }
            }

            String authorId = kookEventDTO.getAuthorId();
            String replyMsg = String.format(KookReplyMsgTemplateEnum.TEXT_MSG.getMsg(), authorId, format);
            return new KookReplyMsgDTO()
                    .setApiEndPoint("/api/v3/direct-message/create")
                    .setEventKey(baseEvent.getEventKey())
                    .setMsg(replyMsg);
        }
        return null;
    }

}
