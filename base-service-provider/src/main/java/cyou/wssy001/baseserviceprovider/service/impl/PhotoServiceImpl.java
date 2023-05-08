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
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import static java.awt.RenderingHints.*;
import static java.awt.font.TextAttribute.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class PhotoServiceImpl implements PhotoService, ApplicationListener<ContextRefreshedEvent> {
    private final HttpClient httpClient;
    private final FileCacheService fileCacheService;

    private static final CopyOnWriteArrayList<PhotoInfo> photoInfoList = new CopyOnWriteArrayList<>();


    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.info("******PhotoServiceImpl.onApplicationEvent：正在初始化图片数据");

        List<PhotoInfo> photos = fileCacheService.getPhotos();
        if (CollUtil.isEmpty(photos)) {
            refreshPhotos(false);
            fileCacheService.cachePhotos(photoInfoList);
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
        ClassPathResource classPathResource = new ClassPathResource("nukaCodeBG.png");
        if (!classPathResource.exists()) {
            log.error("******PhotoServiceImpl.createNukaCodePhoto：没有找到 nukaCodeBG.png，请检查 base-service-provider/src/main/resources/ 下有无 nukaCodeBG.png 文件");
            return false;
        }

        try (InputStream inputStream = classPathResource.getInputStream()) {
            if (!file.exists()) file.createNewFile();

            BufferedImage image = ImageIO.read(inputStream);
            Graphics2D pen = image.createGraphics();
            HashMap<TextAttribute, Object> hm = new HashMap<>();
            hm.put(TextAttribute.SIZE, 130.34);
            hm.put(WEIGHT, WEIGHT_REGULAR);
            hm.put(TextAttribute.TRACKING, TRACKING_TIGHT);
            hm.put(TextAttribute.FAMILY, "Share-TechMono Regular");
            Font font = new Font(hm);
            pen.setFont(font);
            pen.setColor(Color.WHITE);
            pen.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);
            pen.setRenderingHint(KEY_TEXT_ANTIALIASING, VALUE_TEXT_ANTIALIAS_ON);
            pen.drawString(nukaCode.getAlpha(), 1021, 485);
            pen.drawString(nukaCode.getBravo(), 1072, 637);
            pen.drawString(nukaCode.getCharlie(), 1186, 779);

            HashMap<TextAttribute, Object> hm2 = new HashMap<>();
            hm2.put(TextAttribute.UNDERLINE, 2);
            hm2.put(TextAttribute.SIZE, 50.27);
            hm2.put(TextAttribute.WEIGHT, WEIGHT_BOLD);
            hm2.put(TextAttribute.FAMILY, "Microsoft YaHei");
            Font font2 = new Font(hm2);
            pen.setFont(font2);
            pen.setColor(new Color(216, 206, 108));
            String timeString = DateUtil.format(nukaCode.getStartTime(), "yyyy年MM月dd日") + " - " + DateUtil.format(nukaCode.getExpireTime(), "MM月dd日");
            pen.drawString(timeString, 1106, 918);
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
        PhotoServiceImpl.photoInfoList.addAll(photoInfoList);
    }
}
