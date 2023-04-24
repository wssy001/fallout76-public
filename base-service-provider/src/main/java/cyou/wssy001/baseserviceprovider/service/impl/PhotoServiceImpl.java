package cyou.wssy001.baseserviceprovider.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONArray;
import cyou.wssy001.common.entity.PhotoInfo;
import cyou.wssy001.common.enums.HttpEnum;
import cyou.wssy001.common.enums.PlatformEnum;
import cyou.wssy001.common.service.FileCacheService;
import cyou.wssy001.common.service.PhotoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@Component
@RequiredArgsConstructor
public class PhotoServiceImpl implements PhotoService, ApplicationListener<ContextRefreshedEvent> {
    private final HttpClient httpClient;
    private final FileCacheService fileCacheService;

    private final CopyOnWriteArrayList<PhotoInfo> photoInfoList = new CopyOnWriteArrayList<>();


    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.info("******PhotoServiceImpl.onApplicationEvent：正在初始化图片数据");

        List<PhotoInfo> photos = fileCacheService.getPhotos();
        if (CollUtil.isEmpty(photos)) {
            refreshPhotos(false);
            fileCacheService.cachePhotos(this.photoInfoList);
        } else {
            updatePhotos(photos);
        }

        log.info("******PhotoServiceImpl.onApplicationEvent：初始化图片数据完毕");
    }

    @Override
    public LinkedHashMap<String, String> getPhotoUrls(String name, PlatformEnum platform) {
        return photoInfoList.stream()
                .filter(photoInfo -> photoInfo.getKey().equals(name) && (platform == null || photoInfo.getPlatform().equals(platform)))
                .findFirst()
                .map(PhotoInfo::getUrlMap)
                .orElseGet(() -> new LinkedHashMap<>(0));
    }

    @Override
    public List<PhotoInfo> getPhotos() {
        return photoInfoList;
    }

    @Override
    @RegisterReflectionForBinding(PhotoInfo.class)
    public boolean refreshPhotos(boolean cache) {
        log.info("******PhotoServiceImpl.refreshPhotos：正在获取最新的图片信息");
        String body = "";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(HttpEnum.PHOTOS_WEB_URL.getValue()))
                .header(HttpHeaders.USER_AGENT, HttpEnum.USER_AGENT.getValue())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .GET()
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            body = response.body();
            int code = response.statusCode();
            if (code != 200) {
                log.error("******PhotoServiceImpl.refreshNukaCode：请求发送失败，状态码：{}，结果：{}", code, body);
                return false;
            }

            log.info("******PhotoServiceImpl.refreshNukaCode：请求发送成功");
        } catch (Exception e) {
            log.error("******PhotoServiceImpl.refreshNukaCode：无法建立连接，原因：{}，返回体：{}", e.getMessage(), body);
            return false;
        }

        if (StrUtil.isBlank(body)) {
            log.error("******PhotoServiceImpl.refreshNukaCode：读取Response失败");
            return false;
        }

        log.info("******PhotoServiceImpl.refreshPhotos：图片信息获取成功");
        List<PhotoInfo> photoInfoList = JSONArray.parseArray(body, PhotoInfo.class);
        if (cache) fileCacheService.cachePhotos(photoInfoList);
        updatePhotos(photoInfoList);
        return true;
    }

    private void updatePhotos(List<PhotoInfo> photoInfoList) {
        this.photoInfoList.addAll(photoInfoList);
    }
}
