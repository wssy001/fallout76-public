package cyou.wssy001.qqadopter.service;

import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import cyou.wssy001.common.entity.PhotoInfo;
import cyou.wssy001.common.enums.HttpEnum;
import cyou.wssy001.common.enums.PlatformEnum;
import cyou.wssy001.common.service.PhotoService;
import cyou.wssy001.qqadopter.config.QQConfig;
import cyou.wssy001.qqadopter.dto.QQFileUploadEventDTO;
import cyou.wssy001.qqadopter.enums.QQReplyMsgTemplateEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;

/**
 * @Description: 日替图片更新服务类
 * @Author: Tyler
 * @Date: 2023/5/17 20:51
 * @Version: 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WeeklyOffersUpdateService {
    private final QQConfig qqConfig;
    private final HttpClient httpClient;
    private final PhotoService photoService;


    @Async
    public void updateWeeklyOffers(QQFileUploadEventDTO qqFileUploadEventDTO) {
        if (qqFileUploadEventDTO.getUserId() != 1137631718L || qqFileUploadEventDTO.getGroupId() != 733491495L)
            return;

        JSONObject file = qqFileUploadEventDTO.getFile();
        String accessToken = qqConfig.getAccessToken() == null ? "" : qqConfig.getAccessToken();
        String imageUrl = file.getString("url");
        String format = String.format(QQReplyMsgTemplateEnum.WEEKLY_OFFERS_MSG_TEMPLATE.getMsg(), imageUrl);
        String replyMsg = String.format(QQReplyMsgTemplateEnum.GROUP_TEXT_MSG.getMsg(), 1080713430, format);
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(qqConfig.getGoCqhttpUrl() + "/send_group_msg"))
                .header(HttpHeaders.USER_AGENT, HttpEnum.USER_AGENT.getValue())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .POST(HttpRequest.BodyPublishers.ofString(replyMsg));
        HttpRequest request = builder.build();
        String body = null;
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            int code = response.statusCode();
            body = response.body();
            if (code != 200) {
                log.error("******WeeklyOffersUpdateService.updateWeeklyOffers：消息发送失败，状态码：{}，原因：{}", code, body);
                return;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        body = JSON.parseObject(body)
                .getJSONObject("data")
                .toJSONString();
        request = builder.uri(URI.create(qqConfig.getGoCqhttpUrl() + "/get_msg"))
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            int code = response.statusCode();
            body = response.body();
            if (code != 200) {
                log.error("******WeeklyOffersUpdateService.updateWeeklyOffers：消息获取失败，状态码：{}，原因：{}", code, body);
                return;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        String imageId = ReUtil.getGroup0("^file=.*.image$", body);
        if (StrUtil.isBlank(imageId)) return;
        imageId = imageId.replace("file=", "");

        body = """
                {
                    "image": "%s"
                }
                """;
        request = builder.uri(URI.create(qqConfig.getGoCqhttpUrl() + "/ocr_image"))
                .POST(HttpRequest.BodyPublishers.ofString(String.format(body, imageId)))
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            int code = response.statusCode();
            body = response.body();
            if (code != 200) {
                log.error("******WeeklyOffersUpdateService.updateWeeklyOffers：图片OCR失败，状态码：{}，原因：{}", code, body);
                return;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        JSONObject response = JSON.parseObject(body);
        response = response.getJSONObject("data")
                .getJSONArray("texts")
                .stream()
                .map((obj) -> (JSONObject) obj)
                .filter(obj -> obj.getString("text").contains("原子商店日替") && obj.getInteger("confidence") > 70)
                .findFirst()
                .orElse(null);
        if (response == null) return;

        HashMap<String, String> map = new HashMap<>();
        map.put("1", imageUrl);
        PhotoInfo photoInfo = new PhotoInfo()
                .setPlatform(PlatformEnum.QQ)
                .setKey("weeklyOffers")
                .setUrlMap(map);
        photoService.updatePhotoUrl(photoInfo);
        photoInfo.setPlatform(PlatformEnum.QQ_GUILD);
        photoService.updatePhotoUrl(photoInfo);
    }
}
