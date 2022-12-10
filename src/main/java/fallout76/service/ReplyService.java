package fallout76.service;

import fallout76.restapi.GoCQHttpApiService;
import fallout76.restapi.KookApiService;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.core.Response;

@Singleton
public class ReplyService {
    private static final Logger LOG = Logger.getLogger(ReplyService.class);

    @Inject
    @RestClient
    GoCQHttpApiService goCQHttpApiService;

    @Inject
    @RestClient
    KookApiService kookApiService;


    public void sendQQGroupMessage(String body, String key) {
        Response response = goCQHttpApiService.sendGroupMessage(body);
        handleResponse(response, key);
    }

    public void sendQQPrivateMessage(String body, String key) {

    }

    public void sendQQGuildChannelMessage(String body, String key) {

    }


    public void handleResponse(Response response, String key) {
        int status = response.getStatus();

        if (status != 200) {
            LOG.errorf("回复 %s 失败，Http Code：%s", key, status);
            return;
        }

        LOG.infof("回复 %s 成功", key);
    }
}
