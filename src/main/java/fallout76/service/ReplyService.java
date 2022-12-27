package fallout76.service;

import com.fasterxml.jackson.databind.JsonNode;
import fallout76.entity.KookLimit;
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
    KookAPILimiterService kookAPILimiterService;

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
        boolean permit = kookAPILimiterService.checkPermit("message/create");
        if (!permit) log.error("******ReplyService.sendKookGuildChannelMessage：回复 {} 失败，API限速", key);
        Response response = kookApiService.sendGuildMessage(body);
        handleResponse(response, "Kook Guild：" + key);
        handleKookAPILimit(response);
    }

    public void sendKookGuildPrivateMessage(String body, String key) {
        Response response = kookApiService.sendGuildPrivateMessage(body);
        handleResponse(response, "Kook Private：" + key);
        handleKookAPILimit(response);
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

    public void handleKookAPILimit(Response response) {
        String limit = response.getHeaderString("X-Rate-Limit-Limit");
        String remaining = response.getHeaderString("X-Rate-Limit-Remaining");
        String reset = response.getHeaderString("X-Rate-Limit-Reset");
        String bucket = response.getHeaderString("X-Rate-Limit-Bucket");

        KookLimit kookLimit = new KookLimit()
                .setBucket(bucket)
                .setLimit(Integer.parseInt(limit))
                .setRemaining(Integer.parseInt(remaining))
                .setResetWait(Long.parseLong(reset));
        kookAPILimiterService.updateBucketKookLimitMap(bucket, kookLimit);
    }

}
