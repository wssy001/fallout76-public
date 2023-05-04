package cyou.wssy001.dodoadopter.service;

import cn.hutool.core.util.StrUtil;
import cyou.wssy001.common.dto.BaseReplyMsgDTO;
import cyou.wssy001.common.enums.HttpEnum;
import cyou.wssy001.common.enums.PlatformEnum;
import cyou.wssy001.common.service.ReplyService;
import cyou.wssy001.dodoadopter.config.DoDoConfig;
import cyou.wssy001.dodoadopter.dto.DoDoReplyMsgDTO;
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
 * @Description: DoDo消息回复服务实现类
 * @Author: Tyler
 * @Date: 2023/3/16 10:07
 * @Version: 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DoDoReplyService implements ReplyService {
    private final DoDoConfig dodoConfig;
    private final HttpClient httpClient;


    @Override
    public List<PlatformEnum> getPlatforms() {
        return List.of(PlatformEnum.DODO, PlatformEnum.DODO);
    }

    @Override
    public void reply(BaseReplyMsgDTO msg) {
        if (msg instanceof DoDoReplyMsgDTO DoDoReplyMsgDTO) {
            log.info("******DoDoReplyService.reply：正在回复：{} 指令", DoDoReplyMsgDTO.getEventKey());
            String clientId = dodoConfig.getClientId();
            String botToken = dodoConfig.getBotToken();
            if (StrUtil.hasBlank(clientId, botToken)) {
                log.error("******DoDoReplyService.reply：回复失败，请先配置robot-config.dodo.client-id和robot-config.dodo.bot-token");
                return;
            }

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(dodoConfig.getOpenApiBaseUrl() + DoDoReplyMsgDTO.getApiEndPoint()))
                    .header(HttpHeaders.USER_AGENT, HttpEnum.USER_AGENT.getValue())
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .header(HttpHeaders.AUTHORIZATION, String.format("Bot %s.%s", clientId, botToken))
                    .POST(HttpRequest.BodyPublishers.ofString(DoDoReplyMsgDTO.getMsg()))
                    .build();

            log.debug("******DoDoReplyService.reply：准备发送回复消息，消息内容：\n{}", DoDoReplyMsgDTO.getMsg());
            try {
                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
                String body = response.body();
                int code = response.statusCode();
                if (code != 200) {
                    log.error("******DoDoReplyService.reply：回复消息发送失败，状态码：{}，原因：{}", code, body);
                    return;
                }

                log.info("******DoDoReplyService.reply：回复消息发送成功，结果：{}", body);
            } catch (Exception e) {
                log.error("******DoDoReplyService.reply：回复：{} 失败，原因：{}", DoDoReplyMsgDTO.getEventKey(), e.getMessage());
            }
        }
    }

    @Override
    @RegisterReflectionForBinding(DoDoReplyMsgDTO.class)
    public void reply(BaseReplyMsgDTO msg, Object target) {
        reply(msg);
    }
}
