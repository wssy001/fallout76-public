package cyou.wssy001.dodoadopter.handler;

import cn.hutool.core.collection.CollUtil;
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
public class WeeklyOffersUpdateDoDoEventHandler implements BaseHandler {
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
        return Set.of("/更新日替");
    }

    @Override
    public String getDescription() {
        return "更新原子商店特惠预览图";
    }

    @Override
    public BaseReplyMsgDTO consume(BaseEvent baseEvent, BasePlatformEventDTO basePlatformEventDTO) {
        if (baseEvent instanceof BaseAdminEvent && basePlatformEventDTO instanceof DoDoEventDTO dodoEventDTO) {
            DoDoEventDTO.EventBody dodoEventDTOData = dodoEventDTO.getData();
            JSONObject eventBody = dodoEventDTOData.getEventBody();
            String content = eventBody.getJSONObject("messageBody")
                    .getString("content");
            String url = ReUtil.getGroup0(Validator.URL_HTTP.pattern(), content);
            boolean updated = false;
            url = dodoResourceUploadService.upload(url);
            if (StrUtil.isNotBlank(url)) {
                Map<String, String> map = new HashMap<>();
                map.put("1", url);
                PhotoInfo photoInfo = new PhotoInfo()
                        .setPlatform(PlatformEnum.DODO)
                        .setKey("weeklyOffers")
                        .setUrlMap(map);
                updated = photoService.updatePhotoUrl(photoInfo);
            }

            String format;
            if (updated && photoService.storePhotoCache()) {
                format = String.format(DoDoReplyMsgTemplateEnum.TEXT_MSG_TEMPLATE.getMsg(), "原子商店特惠预览图更新成功");
            } else {
                log.error("******WeeklyOffersUpdateDoDoEventHandler.consume：原子商店特惠预览图更新失败");
                format = String.format(DoDoReplyMsgTemplateEnum.TEXT_MSG_TEMPLATE.getMsg(), "原子商店特惠预览图更新失败");
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
