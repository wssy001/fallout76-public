package cyou.wssy001.baseserviceprovider.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ReUtil;
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
import org.springframework.scheduling.annotation.Async;
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
import java.util.List;
import java.util.*;
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

    @Async
    @Override
    public void createNukaCodePhoto(String name, NukaCode nukaCode) {
        log.info("******PhotoServiceImpl.createNukaCodePhoto：正在生成图片：{}", name);
        File file = new File(PathUtil.getJarPath() + "/config/nukacode/" + name);
        ClassPathResource nukaCodeBGResource = new ClassPathResource("nukacode/nukaCodeBG.png");
        if (!nukaCodeBGResource.exists()) {
            log.error("******PhotoServiceImpl.createNukaCodePhoto：没有找到 nukaCodeBG.png，请检查 base-service-provider/src/main/resources/nukacode 下有无 nukaCodeBG.png 文件");
            return;
        }

        try (InputStream inputStream = nukaCodeBGResource.getInputStream()) {
            if (!file.exists() && !file.createNewFile()) {
                log.error("******PhotoServiceImpl.createNukaCodePhoto：生成临时文件：{} 失败", file.getPath());
                return;
            }

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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Async
    @Override
    public void createHelpPhoto(String name, HashMap<Set<String>, String> map) {
        ClassPathResource headResource = new ClassPathResource("help/head.png");
        ClassPathResource bodyResource = new ClassPathResource("help/body.png");
        ClassPathResource tailResource = new ClassPathResource("help/tail.png");
        if (!headResource.exists() || !bodyResource.exists() || !tailResource.exists()) {
            log.error("******PhotoServiceImpl.createHelpPhoto：没有找到相应文件，base-service-provider/src/main/resources/help 下有无 head.png、body.png、tail.png 文件");
            return;
        }

        try (InputStream head = headResource.getInputStream();
             InputStream body = bodyResource.getInputStream();
             InputStream tail = tailResource.getInputStream()) {

            File file = new File(PathUtil.getJarPath() + "/config/help/" + name);
            if (!file.exists() && !file.createNewFile()) {
                log.error("******PhotoServiceImpl.createHelpPhoto：生成临时文件：{} 失败", file.getPath());
                return;
            }

            BufferedImage headImage = ImageIO.read(head);
            BufferedImage bodyImage = ImageIO.read(body);
            BufferedImage tailImage = ImageIO.read(tail);

            int bodyImageCount = getBodyImageCount(map, 100, bodyImage.getWidth() - 200, bodyImage.getHeight() - 200);
            int height = headImage.getHeight() + bodyImage.getHeight() * bodyImageCount + tailImage.getHeight();
            int width = headImage.getWidth();
            BufferedImage baseImage = new BufferedImage(width, height, headImage.getType());
            Graphics2D pen = baseImage.createGraphics();
            pen.setRenderingHint(KEY_RENDERING, VALUE_RENDER_QUALITY);
            pen.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);
            pen.setRenderingHint(KEY_COLOR_RENDERING, VALUE_COLOR_RENDER_QUALITY);
            pen.setRenderingHint(KEY_TEXT_ANTIALIASING, VALUE_TEXT_ANTIALIAS_ON);

            // 渲染头图片
            int y = 0;
            pen.drawImage(headImage, 0, y, headImage.getWidth(), headImage.getHeight(), null);
            y += headImage.getHeight();
            int bodyHeight = y;

            // 渲染身体图片集合
            for (int i = 0; i < bodyImageCount; i++) {
                pen.drawImage(bodyImage, 0, y, bodyImage.getWidth(), bodyImage.getHeight(), null);
                y += bodyImage.getHeight();
            }

            // 渲染文字指令
            HashMap<TextAttribute, Object> hashMap = new HashMap<>();
            hashMap.put(TextAttribute.WEIGHT, WEIGHT_BOLD);
            hashMap.put(TextAttribute.FAMILY, "Microsoft YaHei");
            pen.setColor(Color.WHITE);

            int fontSize = 100;
            for (Entry<Set<String>, String> next : map.entrySet()) {
                hashMap.put(TextAttribute.SIZE, fontSize);
                Font font = new Font(hashMap);
                pen.setFont(font);

                for (String line : generateKeyLines(next.getKey(), font.getSize(), bodyImage.getWidth() - 200)) {
                    pen.drawString(line, 0, bodyHeight);
                    bodyHeight += 1.3 * fontSize;
                }

                hashMap.put(TextAttribute.SIZE, fontSize * 0.7);
                font = new Font(hashMap);
                pen.setFont(font);
                for (String line : generateDescriptionLines(next.getValue(), font.getSize(), bodyImage.getWidth() - 200)) {
                    pen.drawString(line, 0, bodyHeight);
                    bodyHeight += 1.3 * fontSize;
                }
                bodyHeight += 1.3 * fontSize;
            }

            // 渲染尾图片
            pen.drawImage(tailImage, 0, y, tailImage.getWidth(), tailImage.getHeight(), null);
            ImageIO.write(baseImage, "png", file);
            pen.dispose();
            baseImage.flush();
            log.info("******PhotoServiceImpl.createHelpPhoto：图片：{} 生成成功，路径：{}", name, file.getPath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean updatePhotoUrl(PhotoInfo photoInfo) {
        photoInfoList.removeIf(record -> record.getKey().equals(photoInfo.getKey()) && record.getPlatform().equals(photoInfo.getPlatform()));
        photoInfoList.add(photoInfo);
        return true;
    }

    @Override
    public boolean storePhotoCache() {
        return fileCacheService.cachePhotos(photoInfoList);
    }

    private void updatePhotos(List<PhotoInfo> photoInfoList) {
        PhotoServiceImpl.photoInfoList.addAll(photoInfoList);
    }

    private int getBodyImageCount(Map<Set<String>, String> commandMap, int fontSize, int imageWidth, int imageHeight) {
        double totalHeight = 0;
        Iterator<Entry<Set<String>, String>> iterator = commandMap.entrySet()
                .iterator();
        while (iterator.hasNext()) {
            Entry<Set<String>, String> entry = iterator.next();
            int keysLineCount = calcKeysLines(entry.getKey(), fontSize, imageWidth);
            int descriptionLineCount = calcDescriptionLines(entry.getValue(), 0.7 * fontSize, imageWidth);
            totalHeight += fontSize * (keysLineCount + descriptionLineCount);
            totalHeight += 0.3 * fontSize * (keysLineCount + descriptionLineCount - 2);
            if (iterator.hasNext()) totalHeight += 1.3 * fontSize;
        }
        return (int) Math.ceil(totalHeight / imageHeight);
    }

    /**
     * 一个汉字     1倍      fontSize
     * 一个英文/    0.5倍    fontSize
     * 4个空格     1倍      fontSize
     */
    private int calcKeysLines(Set<String> keys, double fontSize, int imageWidth) {
        List<String> temp = new ArrayList<>(List.copyOf(keys));
        temp.sort(Comparator.comparingInt(String::length));
        int lineCount = 1;
        // 预留一个汉字宽度
        double currentLineWidth = fontSize;

        while (!temp.isEmpty()) {
            double keyWidth = 0;
            String key = temp.get(0);

            // 英文字符宽度
            keyWidth += 0.5 * fontSize;
            // 汉字宽度
            keyWidth += ReUtil.count("[一-颉]", key) * fontSize;

            if (currentLineWidth + keyWidth >= imageWidth) {
                lineCount += 1;
                currentLineWidth = 100;
            }

            currentLineWidth += keyWidth;
            temp.remove(key);
        }

        return lineCount;
    }

    private int calcDescriptionLines(String description, double fontSize, int imageWidth) {
        int lineCount = 1;
        // 预留二个汉字宽度
        double currentLineWidth = 2 * fontSize;
        if (currentLineWidth + description.length() * fontSize <= imageWidth)
            return 1;

        for (int i = 0; i < description.length(); i++) {
            currentLineWidth += fontSize;
            if (currentLineWidth > imageWidth) {
                lineCount += 1;
                currentLineWidth = 0;
            }
        }

        return lineCount;
    }

    private List<String> generateKeyLines(Set<String> keys, double fontSize, int imageWidth) {
        List<String> temp = new ArrayList<>(List.copyOf(keys));
        temp.sort(Comparator.comparingInt(String::length));

        List<String> lines = new ArrayList<>();
        StringBuilder line = new StringBuilder();

        // 预留一个汉字宽度
        double currentLineWidth = fontSize;
        line.append("    ");

        while (!temp.isEmpty()) {
            double keyWidth = 0;
            String key = temp.get(0);

            // 英文字符宽度
            keyWidth += 0.5 * fontSize;
            // 汉字宽度
            keyWidth += ReUtil.count("[一-颉]", key) * fontSize;

            if (currentLineWidth + keyWidth >= imageWidth) {
                lines.add(line.toString());
                line.setLength(0);
                currentLineWidth = 100;
                line.append("    ");
            }

            currentLineWidth += keyWidth;
            if (!line.isEmpty()) line.append("    ");
            line.append(key);
            temp.remove(key);
        }

        lines.add(line.toString());
        return lines;
    }

    private List<String> generateDescriptionLines(String description, double fontSize, int imageWidth) {
        List<String> lines = new ArrayList<>();
        StringBuilder line = new StringBuilder();

        // 预留二个汉字宽度
        double currentLineWidth = 2 * fontSize;
        line.append("    ")
                .append("    ");

        if (currentLineWidth + description.length() * fontSize <= imageWidth)
            return Collections.singletonList(line.append(description).toString());

        for (int i = 0; i < description.length(); i++) {
            line.append(description.charAt(i));
            currentLineWidth += fontSize;
            if (currentLineWidth > imageWidth) {
                lines.add(line.toString());
                line.setLength(0);
                currentLineWidth = 0;
            }
        }

        lines.add(line.toString());
        return lines;
    }
}
