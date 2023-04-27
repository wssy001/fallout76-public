package cyou.wssy001.baseserviceprovider.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONArray;
import cyou.wssy001.common.entity.NukaCode;
import cyou.wssy001.common.entity.PhotoInfo;
import cyou.wssy001.common.enums.HttpEnum;
import cyou.wssy001.common.enums.PlatformEnum;
import cyou.wssy001.common.service.FileCacheService;
import cyou.wssy001.common.service.PhotoService;
import cyou.wssy001.common.util.PathUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.ZoneOffset;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import static java.awt.RenderingHints.*;

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
    public Map<String, String> getPhotoUrls(String name, PlatformEnum platform) {
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

    @Override
    public boolean createNukaCodePhoto(String name, NukaCode nukaCode) {
        log.info("******PhotoServiceImpl.createNukaCodePhoto：正在生成图片：{}", name);
        File file = new File(PathUtil.getJarPath() + "/config/" + name);
        if (file.exists()) {
            long lastModified = file.lastModified();
            long epochMilli = nukaCode.getExpireTime()
                    .toInstant(ZoneOffset.ofHours(8))
                    .toEpochMilli();
            if (lastModified < epochMilli) {
                log.info("******PhotoServiceImpl.createNukaCodePhoto：已有图片：{} 且未过期，图片生成取消", name);
                return true;
            }
        }

        ClassPathResource classPathResource = new ClassPathResource("nukaCodeBG.png");
        if (!classPathResource.exists()) {
            log.error("******PhotoServiceImpl.createNukaCodePhoto：没有找到 nukaCodeBG.png，请检查 base-service-provider/src/main/resources/ 下有无 nukaCodeBG.png 文件");
            return false;
        }
        try (InputStream inputStream = classPathResource.getInputStream()) {
            BufferedImage image = ImageIO.read(inputStream);
            Graphics2D pen = image.createGraphics();
            pen.setColor(Color.WHITE);
            pen.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);
            pen.setRenderingHint(KEY_TEXT_ANTIALIASING, VALUE_TEXT_ANTIALIAS_ON);
            pen.setFont(new Font("华康少女字体", Font.ITALIC, 40));
            pen.drawString(String.format("A点：\t%s", nukaCode.getAlpha()), 70, 240);
            pen.drawString(String.format("B点：\t%s", nukaCode.getBravo()), 70, 310);
            pen.drawString(String.format("C点：\t%s", nukaCode.getCharlie()), 70, 380);
            pen.setFont(new Font("华康少女字体", Font.PLAIN, 20));
            pen.setColor(Color.BLACK);
            pen.drawString(String.format("过期时间：\t%s", DateUtil.format(nukaCode.getExpireTime(), "MM月dd日 HH时 （北京时间）")), 55, 440);
            ImageIO.write(image, "png", file);
            image.flush();
            log.info("******PhotoServiceImpl.createNukaCodePhoto：图片：{} 生成成功，路径：{}", name, file.getPath());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void updatePhotos(List<PhotoInfo> photoInfoList) {
        this.photoInfoList.addAll(photoInfoList);
    }
}
