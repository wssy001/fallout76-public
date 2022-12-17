package fallout76.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fallout76.util.PathUtil;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
@Singleton
public class PhotoService {
    @Inject
    ObjectMapper objectMapper;

    private final Map<String, String> photoMap = new ConcurrentHashMap<>();
    private static final Lock lock = new ReentrantLock();


    @PostConstruct
    public void init() {
        log.info("******PhotoService.init：正在读取 photos.json");
        try {
            JsonNode jsonNode = objectMapper.readTree(new File(PathUtil.getJarPath() + "/config/photos.json"));
            updatePhotoMap(jsonNode);
            log.info("******PhotoService.init：读取 photos.json 完毕");
        } catch (IOException e) {
            log.error("******PhotoService.init：读取 photos.json 失败，原因：{}", e.getMessage());
        }
    }

    public void updatePhotoMap(JsonNode jsonNode) {
        try {
            lock.lock();
            Map<String, String> map = objectMapper.convertValue(jsonNode, new TypeReference<>() {
            });
            photoMap.clear();
            photoMap.putAll(map);
        } catch (Exception e) {
            log.error("******PhotoService.updatePhotoMap：更新 photoMap 失败，原因：{}", e.getMessage());
        } finally {
            lock.unlock();
        }

    }

    public String getPhoto(String name) {
        try {
            lock.lock();
            return photoMap.get(name);
        } catch (Exception e) {
            log.error("******PhotoService.getPhoto：获取 {} 失败，原因：{}", name, e.getMessage());
        } finally {
            lock.unlock();
        }
        return null;
    }
}
