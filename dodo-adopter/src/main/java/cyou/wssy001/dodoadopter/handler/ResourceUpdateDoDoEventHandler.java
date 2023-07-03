package cyou.wssy001.dodoadopter.handler;

import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONObject;
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
import cyou.wssy001.dodoadopter.dto.DoDoEventDTO;
import cyou.wssy001.dodoadopter.dto.DoDoReplyMsgDTO;
import cyou.wssy001.dodoadopter.enums.DoDoReplyMsgTemplateEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
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
public class ResourceUpdateDoDoEventHandler implements BaseHandler {
    private final PhotoService photoService;
    private final ResourceUploadService dodoResourceUploadService;


    @Override
    public PlatformEnum getPlatform() {
        return PlatformEnum.DODO;
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
        if (baseEvent instanceof BaseAdminEvent && basePlatformEventDTO instanceof DoDoEventDTO dodoEventDTO) {
            DoDoEventDTO.EventBody dodoEventDTOData = dodoEventDTO.getData();
            JSONObject eventBody = dodoEventDTOData.getEventBody();
            String content = eventBody.getJSONObject("messageBody")
                    .getString("content");
            String[] commandArgs = content.split("\\s+");

            String format;
            if (commandArgs.length != 3) {
                log.error("******ResourceUpdateDoDoEventHandler.consume：资源更新失败，参数异常");
                format = String.format(DoDoReplyMsgTemplateEnum.TEXT_MSG_TEMPLATE.getMsg(), "参数异常，请检查");
            } else {
                String url = ReUtil.getGroup0(Validator.URL_HTTP.pattern(), commandArgs[2]);
                boolean updated = false;
                url = dodoResourceUploadService.upload(url);
                Map<String, String> map = new HashMap<>();
                if (StrUtil.isNotBlank(url)) {
                    map.put("1", url);
                    PhotoInfo photoInfo = new PhotoInfo()
                            .setPlatform(PlatformEnum.DODO)
                            .setKey(commandArgs[1])
                            .setUrlMap(map);
                    updated = photoService.updatePhotoUrl(photoInfo);
                }

                if (updated && photoService.storePhotoCache()) {
                    format = String.format(DoDoReplyMsgTemplateEnum.TEXT_MSG_TEMPLATE.getMsg(), "资源更新成功");
                } else {
                    log.error("******ResourceUpdateDoDoEventHandler.consume：资源更新失败");
                    format = String.format(DoDoReplyMsgTemplateEnum.TEXT_MSG_TEMPLATE.getMsg(), "资源更新失败");
                }
            }

            String eventKey = baseEvent.getEventKey();
            String islandSourceId = eventBody.getString("islandSourceId");
            String dodoSourceId = eventBody.getString("dodoSourceId");
            String replyMsg = String.format(DoDoReplyMsgTemplateEnum.PRIVATE_TEXT_MSG.getMsg(), islandSourceId, dodoSourceId, format);
            return new DoDoReplyMsgDTO()
                    .setApiEndPoint("/api/v2/personal/message/send")
                    .setEventKey(eventKey)
                    .setMsg(replyMsg);
        }
        return null;
    }
}
