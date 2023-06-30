package cyou.wssy001.dodoadopter.service;

import cn.hutool.core.io.IoUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import cyou.wssy001.common.enums.HttpEnum;
import cyou.wssy001.common.service.ResourceUploadService;
import cyou.wssy001.common.util.MultiPartBodyPublisher;
import cyou.wssy001.dodoadopter.config.DoDoConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

/**
 * @Description: DoDo资源上传服务实现类
 * @Author: Tyler
 * @Date: 2023/3/16 10:07
 * @Version: 1.0
 */
@Slf4j
@RequiredArgsConstructor
@Service("dodoResourceUploadService")
public class DoDoResourceUploadService implements ResourceUploadService {
    private final DoDoConfig dodoConfig;
    private final HttpClient httpClient;


    @Override
    public String upload(String url) {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .setHeader(HttpHeaders.USER_AGENT, HttpEnum.USER_AGENT.getValue())
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        HttpRequest request = builder.GET()
                .build();

        log.debug("******DoDoResourceUploadService.upload：准备下载图片");
        MultiPartBodyPublisher publisher = null;
        try {
            HttpResponse<InputStream> response = httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());
            InputStream body = response.body();
            int code = response.statusCode();
            if (code != 200) {
                log.error("******DoDoResourceUploadService.upload：图片下载失败，状态码：{}，原因：{}", code, IoUtil.read(body, StandardCharsets.UTF_8));
                return null;
            }

            publisher = new MultiPartBodyPublisher()
                    .addPart("file", () -> body, "weeklyOffers.png", MediaType.IMAGE_JPEG_VALUE);
        } catch (Exception e) {
            log.error("******DoDoResourceUploadService.upload：从：{} 下载图片失败，原因：{}", url, e.getMessage());
        }

        log.debug("******DoDoResourceUploadService.upload：准备上传图片");
        String clientId = dodoConfig.getClientId();
        String botToken = dodoConfig.getBotToken();
        request = builder
                .setHeader(HttpHeaders.AUTHORIZATION, String.format("Bot %s.%s", clientId, botToken))
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.MULTIPART_FORM_DATA_VALUE + "; boundary=" + publisher.getBoundary())
                .uri(URI.create(dodoConfig.getOpenApiBaseUrl() + "/api/v2/resource/picture/upload"))
                .POST(publisher.build())
                .build();
        try {
            HttpResponse<InputStream> response = httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());
            InputStream body = response.body();
            int code = response.statusCode();
            if (code != 200) {
                log.error("******DoDoResourceUploadService.upload：上传图片失败，状态码：{}，原因：{}", code, IoUtil.read(body, StandardCharsets.UTF_8));
                return null;
            }

            log.info("******DoDoResourceUploadService.upload：上传图片成功，结果：{}", body);
            JSONObject jsonObject = JSON.parseObject(body);
            if (jsonObject.getIntValue("status") == 0) return jsonObject
                    .getJSONObject("data")
                    .getString("url");
            return null;
        } catch (Exception e) {
            log.error("******DoDoResourceUploadService.upload：上传图片失败，原因：{}", e.getMessage());
        }
        return null;
    }

}
