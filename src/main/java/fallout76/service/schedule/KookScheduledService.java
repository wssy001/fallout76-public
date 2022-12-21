package fallout76.service.schedule;

import com.fasterxml.jackson.databind.JsonNode;
import fallout76.restapi.KookBotMarketApiService;
import io.quarkus.scheduler.Scheduled;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.core.Response;

@Slf4j
@Singleton
public class KookScheduledService {

    @Inject
    @RestClient
    KookBotMarketApiService kookBotMarketApiService;


    @Scheduled(cron = "0 0/20 * * * ?")
    public void sendOnlineRequest() {
        try (Response response = kookBotMarketApiService.IamOnline()) {
            int status = response.getStatus();
            JsonNode jsonNode = response.readEntity(JsonNode.class);
            if (status == 200) {
                log.info("******KookScheduledService.sendOnlineRequest：发送在线请求成功，请求结果：{}", jsonNode.toPrettyString());
            } else {
                log.error("******KookScheduledService.sendOnlineRequest：发送在线请求失败，Http code：{}", status);
            }
        } catch (Exception e) {
            log.error("******KookScheduledService.sendOnlineRequest：发送在线请求失败，原因：{}", e.getMessage());
        }
    }
}
