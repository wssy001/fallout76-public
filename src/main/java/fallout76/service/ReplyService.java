package fallout76.service;

import com.fasterxml.jackson.databind.JsonNode;
import fallout76.restapi.GoCQHttpApiService;
import fallout76.restapi.KookApiService;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.core.Response;

@Slf4j
@Singleton
public class ReplyService {

    @Inject
    @RestClient
    GoCQHttpApiService goCQHttpApiService;

    @Inject
    @RestClient
    KookApiService kookApiService;


    public void sendQQGroupMessage(String body, String key) {
        Response response = goCQHttpApiService.sendGroupMessage(body);
        handleResponse(response, "QQ Group：" + key);
    }

    public void sendQQPrivateMessage(String body, String key) {
        Response response = goCQHttpApiService.sendPrivateMessage(body);
        handleResponse(response, "QQ Private：" + key);
    }

    public void sendQQGuildChannelMessage(String body, String key) {
        Response response = goCQHttpApiService.sendGuildChannelMessage(body);
        handleResponse(response, "QQ Guild：" + key);
    }

    public void sendKookGuildChannelMessage(String body, String key) {
        Response response = kookApiService.sendGuildMessage(body);
        handleResponse(response, "Kook Guild：" + key);
    }

    public void sendKookGuildPrivateMessage(String body, String key) {
        Response response = kookApiService.sendGuildPrivateMessage(body);
        handleResponse(response, "Kook Private：" + key);
    }

    public void handleResponse(Response response, String key) {
        int status = response.getStatus();
        if (status != 200) {
            log.error("******ReplyService.handleResponse：回复 {} 失败，Http Code：{}", key, status);
            return;
        }

        JsonNode jsonNode = response.readEntity(JsonNode.class);
        log.info("******ReplyService.handleResponse：回复 {} 成功，返回结果：{}", key, jsonNode.toPrettyString());
    }

}
