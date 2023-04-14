package cyou.wssy001.kookadopter.service;

import com.alibaba.fastjson2.JSON;
import cyou.wssy001.common.dto.BaseReplyMsg;
import cyou.wssy001.common.enums.HttpEnum;
import cyou.wssy001.common.service.ReplyService;
import cyou.wssy001.kookadopter.config.KookConfig;
import cyou.wssy001.kookadopter.dto.KookReplyMsgDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * @Description: 消息回复服务实现类
 * @Author: Tyler
 * @Date: 2023/3/16 10:07
 * @Version: 1.0
 */
@Slf4j
@Async
@RequiredArgsConstructor
@Service("kookReplyService")
public class KookReplyService implements ReplyService {
    private final KookConfig kookConfig;

    private final HttpClient httpClient;


    @Override
    public void reply(BaseReplyMsg msg) {
        if (msg instanceof KookReplyMsgDTO kookReplyMsgDTO) {
            log.info("******KookReplyService.reply：正在回复：{} 指令", kookReplyMsgDTO.getEventKey());

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(kookReplyMsgDTO.getUrl()))
                    .header(HttpHeaders.USER_AGENT, HttpEnum.USER_AGENT.getValue())
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .header(HttpHeaders.AUTHORIZATION, "Bot " + kookConfig.getToken())
                    .POST(HttpRequest.BodyPublishers.ofString(kookReplyMsgDTO.getMsg()))
                    .build();

            log.info("******KookReplyService.reply：准备发送回复消息，消息内容：{}", JSON.toJSONString(kookReplyMsgDTO));
            try {
                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
                String body = response.body();
                int code = response.statusCode();
                if (code != 200) {
                    log.error("******ReplyServiceImpl.reply：回复消息发送失败，状态码：{}，原因：{}", code, body);
                    return;
                }

                log.info("******ReplyServiceImpl.reply：回复消息发送成功，结果：{}", body);
            } catch (Exception e) {
                log.error("******ReplyServiceImpl.reply：回复：{} 失败，原因：{}", kookReplyMsgDTO.getEventKey(), e.getMessage());
            }
        }
    }

    @Override
    public void reply(BaseReplyMsg msg, Object target) {
        reply(msg);
    }
}
