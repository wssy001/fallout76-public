package cyou.wssy001.kookadopter.service;

import cyou.wssy001.common.dto.BaseReplyMsgDTO;
import cyou.wssy001.common.enums.HttpEnum;
import cyou.wssy001.common.enums.PlatformEnum;
import cyou.wssy001.common.service.RateLimitService;
import cyou.wssy001.common.service.ReplyService;
import cyou.wssy001.kookadopter.config.KookConfig;
import cyou.wssy001.kookadopter.dto.KookReplyMsgDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

/**
 * @Description: Kook消息回复服务实现类
 * @Author: Tyler
 * @Date: 2023/3/16 10:07
 * @Version: 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class KookReplyService implements ReplyService {
    private final KookConfig kookConfig;
    private final HttpClient httpClient;
    private final RateLimitService rateLimitService;


    @Override
    public List<PlatformEnum> getPlatforms() {
        return List.of(PlatformEnum.KOOK);
    }

    @Override
    public void reply(BaseReplyMsgDTO msg) {
        if (msg instanceof KookReplyMsgDTO kookReplyMsgDTO) {
            log.info("******KookReplyService.reply：正在回复：{} 指令", kookReplyMsgDTO.getEventKey());

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(kookConfig.getOpenApiBaseUrl() + kookReplyMsgDTO.getApiEndPoint()))
                    .header(HttpHeaders.USER_AGENT, HttpEnum.USER_AGENT.getValue())
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .header(HttpHeaders.AUTHORIZATION, "Bot " + kookConfig.getToken())
                    .POST(HttpRequest.BodyPublishers.ofString(kookReplyMsgDTO.getMsg()))
                    .build();

            log.debug("******KookReplyService.reply：准备发送回复消息，消息内容：\n{}", kookReplyMsgDTO.getMsg());
            try {
                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
                String body = response.body();
                int code = response.statusCode();
                if (code != 200) {
                    log.error("******KookReplyService.reply：回复消息发送失败，状态码：{}，原因：{}", code, body);
                    return;
                }

                updateAPIRateLimit(response.headers().map());
                log.info("******KookReplyService.reply：回复消息发送成功，结果：{}", body);
            } catch (Exception e) {
                log.error("******KookReplyService.reply：回复：{} 失败，原因：{}", kookReplyMsgDTO.getEventKey(), e.getMessage());
            }
        }
    }

    @Override
    @RegisterReflectionForBinding(KookReplyMsgDTO.class)
    public void reply(BaseReplyMsgDTO msg, Object target) {
        reply(msg);
    }

    private void updateAPIRateLimit(Map<String, List<String>> headerMap) {
        String remaining = headerMap.get("X-Rate-Limit-Remaining")
                .get(0);
        String resetTime = headerMap.get("X-Rate-Limit-Reset")
                .get(0);
        String remoteEndpoint = headerMap.get("X-Rate-Limit-Bucket")
                .get(0);
        log.debug("******KookReplyService.updateAPIRateLimit：X-Rate-Limit-Reset：{} X-Rate-Limit-Bucket：{} X-Rate-Limit-Remaining：{}", resetTime, remoteEndpoint, remaining);
        rateLimitService.updateRemoteEndpointLimit(remoteEndpoint, Integer.parseInt(resetTime), PlatformEnum.KOOK);
    }
}
