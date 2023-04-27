package cyou.wssy001.baseserviceprovider.service.impl;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import cyou.wssy001.common.entity.NukaCode;
import cyou.wssy001.common.enums.HttpEnum;
import cyou.wssy001.common.service.FileCacheService;
import cyou.wssy001.common.service.NukaCodeService;
import cyou.wssy001.common.service.PhotoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @Description: 核弹密码服务实现类
 * @Author: Tyler
 * @Date: 2023/3/15 14:08
 * @Version: 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NukaCodeServiceImpl implements NukaCodeService, ApplicationListener<ContextRefreshedEvent> {
    private final HttpClient httpClient;
    private final PhotoService photoService;
    private final FileCacheService fileCacheService;

    private final AtomicReference<NukaCode> nukaCode = new AtomicReference<>();


    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.info("******NukaCodeServiceImpl.onApplicationEvent：正在初始化核弹密码");

        NukaCode nukaCode = fileCacheService.getNukaCode();
        if (nukaCode == null) {
            refreshNukaCode(false);
            fileCacheService.cacheNukaCode(this.nukaCode.get());
        } else if (nukaCode.getExpireTime().isBefore(LocalDateTime.now(ZoneId.of("GMT+8")))) {
            log.info("******NukaCodeServiceImpl.onApplicationEvent：文件缓存过期，正在读取最新数据");
            refreshNukaCode(false);
            fileCacheService.cacheNukaCode(this.nukaCode.get());
        } else {
            updateNukaCode(nukaCode);
        }

        log.info("******NukaCodeServiceImpl.onApplicationEvent：初始化核弹密码完毕");
    }

    @RegisterReflectionForBinding(NukaCode.class)
    public boolean refreshNukaCode(boolean cache) {
        log.info("******NukaCodeServiceImpl.refreshNukaCode：正在获取最新的核弹密码");
        String body = "";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(HttpEnum.NUKA_CODE_WEB_URL.getValue()))
                .header(HttpHeaders.USER_AGENT, HttpEnum.USER_AGENT.getValue())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .POST(HttpRequest.BodyPublishers.ofString(HttpEnum.NUKA_CODE_WEB_QUERY_BODY.getValue()))
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            body = response.body();
            int code = response.statusCode();
            if (code != 200) {
                log.error("******NukaCodeServiceImpl.refreshNukaCode：请求发送失败，状态码：{}，结果：{}", code, body);
                return false;
            }

            log.info("******NukaCodeServiceImpl.refreshNukaCode：请求发送成功");
        } catch (Exception e) {
            log.error("******NukaCodeServiceImpl.refreshNukaCode：无法建立连接，原因：{}，返回体：{}", e.getMessage(), body);
            return false;
        }

        if (StrUtil.isBlank(body)) {
            log.error("******NukaCodeServiceImpl.refreshNukaCode：读取Response失败");
            return false;
        }

        log.info("******NukaCodeServiceImpl.refreshNukaCode：核弹密码获取成功");
        JSONObject jsonObject = JSON.parseObject(body);
        JSONObject nukaCodes = jsonObject.getJSONObject("data")
                .getJSONObject("nukeCodes");

        LocalDateTime now = LocalDateTimeUtil.beginOfDay(LocalDateTime.now(ZoneId.of("GMT+8")));
        int week = now.getDayOfWeek().getValue();
        if (week == 7) week = 0;
        LocalDateTime expireTime = now.plusDays(7 - week)
                .plusHours(8);
        NukaCode nukaCode = nukaCodes.toJavaObject(NukaCode.class);
        nukaCode.setExpireTime(expireTime);
        if (cache) fileCacheService.cacheNukaCode(nukaCode);
        photoService.createNukaCodePhoto("nukaCode.png", nukaCode);
        return updateNukaCode(nukaCode);
    }

    public boolean updateNukaCode(NukaCode nukaCode) {
        NukaCode oldValue = this.nukaCode.get();
        return this.nukaCode.compareAndSet(oldValue, nukaCode);
    }

    public NukaCode getNukaCode() {
        return nukaCode.get();
    }

}
