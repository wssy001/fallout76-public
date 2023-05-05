package cyou.wssy001.qqadopter.service;

import cyou.wssy001.common.dto.BaseReplyMsgDTO;
import cyou.wssy001.common.enums.HttpEnum;
import cyou.wssy001.common.enums.PlatformEnum;
import cyou.wssy001.common.service.ReplyService;
import cyou.wssy001.qqadopter.config.QQConfig;
import cyou.wssy001.qqadopter.dto.QQReplyMsgDTO;
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

/**
 * @Description: QQ消息回复服务实现类
 * @Author: Tyler
 * @Date: 2023/3/16 10:07
 * @Version: 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class QQReplyService implements ReplyService {
    private final QQConfig qqConfig;
    private final HttpClient httpClient;


    @Override
    public List<PlatformEnum> getPlatforms() {
        return List.of(PlatformEnum.QQ);
    }

    @Override
    public void reply(BaseReplyMsgDTO msg) {
        if (msg instanceof QQReplyMsgDTO qqReplyMsgDTO) {
            log.info("******QQReplyService.reply：正在回复：{} 指令", qqReplyMsgDTO.getEventKey());
            String accessToken = qqConfig.getAccessToken() == null ? "" : qqConfig.getAccessToken();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(qqConfig.getGoCqhttpUrl() + qqReplyMsgDTO.getApiEndPoint()))
                    .header(HttpHeaders.USER_AGENT, HttpEnum.USER_AGENT.getValue())
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    .POST(HttpRequest.BodyPublishers.ofString(qqReplyMsgDTO.getMsg()))
                    .build();

            log.debug("******QQReplyService.reply：准备发送回复消息，消息内容：\n{}", qqReplyMsgDTO.getMsg());
            try {
                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
                String body = response.body();
                int code = response.statusCode();
                if (code != 200) {
                    log.error("******QQReplyService.reply：回复消息发送失败，状态码：{}，原因：{}", code, body);
                    return;
                }

                log.info("******QQReplyService.reply：回复消息发送成功，结果：{}", body);
            } catch (Exception e) {
                log.error("******QQReplyService.reply：回复：{} 失败，原因：{}", qqReplyMsgDTO.getEventKey(), e.getMessage());
            }
        }
    }

    @Override
    @RegisterReflectionForBinding(QQReplyMsgDTO.class)
    public void reply(BaseReplyMsgDTO msg, Object target) {
        reply(msg);
    }
}
