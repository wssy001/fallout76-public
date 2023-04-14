package cyou.wssy001.kookadopter.service.schedule;

import cn.hutool.core.util.StrUtil;
import cyou.wssy001.common.enums.HttpEnum;
import cyou.wssy001.kookadopter.config.KookConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.TimeUnit;

/**
 * @Description: Kook机器人市场定时心跳服务类
 * @Author: Tyler
 * @Date: 2023/3/16 09:28
 * @Version: 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BotMarketScheduledService {
    private final KookConfig kookConfig;
    private final HttpClient httpClient;


    // 每隔30分钟发送一次心跳
    @Scheduled(fixedDelay = 30, timeUnit = TimeUnit.MINUTES)
    public void heartBeat() {
        String botMarketUUID = kookConfig.getBotMarketUUID();
        String url = kookConfig.getBotMarketOnlineApiUrl();
        if (StrUtil.hasBlank(botMarketUUID, url)) {
            log.debug("******BotMarketScheduledService.heartBeat：未配置Bot Market参数，心跳发送取消");
            return;
        }

        log.info("******BotMarketScheduledService.heartBeat：心跳正在发送");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header(HttpHeaders.USER_AGENT, HttpEnum.USER_AGENT.getValue())
                .header("uuid", botMarketUUID)
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            String body = response.body();
            int code = response.statusCode();
            if (code != 200) {
                log.error("******BotMarketScheduledService.heartBeat：心跳发送失败，状态码：{}，返回体：{}", code, body);
                return;
            }

            log.info("******BotMarketScheduledService.heartBeat：心跳发送成功");
        } catch (Exception e) {
            log.error("******BotMarketScheduledService.heartBeat：建议连接失败，原因：{}", e.getMessage());
        }
    }
}
