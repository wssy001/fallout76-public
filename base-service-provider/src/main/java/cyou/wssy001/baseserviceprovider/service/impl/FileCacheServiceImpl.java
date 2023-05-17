package cyou.wssy001.baseserviceprovider.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileWriter;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import cyou.wssy001.common.entity.NukaCode;
import cyou.wssy001.common.entity.PhotoInfo;
import cyou.wssy001.common.service.FileCacheService;
import cyou.wssy001.common.util.PathUtil;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

/**
 * @Description: 文件缓存服务实现类
 * @Author: Tyler
 * @Date: 2023/3/17 20:16
 * @Version: 1.0
 */
@Slf4j
@Service
public class FileCacheServiceImpl implements FileCacheService {

    @PostConstruct
    public void init() {
        File file = new File(PathUtil.getJarPath() + "/config/nukacode/");
        if (!file.exists()) FileUtil.createTempFile(file).delete();

        file = new File(PathUtil.getJarPath() + "/config/photo/");
        if (!file.exists()) FileUtil.createTempFile(file).delete();

        file = new File(PathUtil.getJarPath() + "/config/help/");
        if (!file.exists()) FileUtil.createTempFile(file).delete();
    }

    @Override
    @RegisterReflectionForBinding(NukaCode.class)
    public NukaCode getNukaCode() {
        File file = new File(PathUtil.getJarPath() + "/config/nukacode/nukaCode.json");
        log.info("******FileCacheServiceImpl.getNukaCode：正在读取核弹密码文件缓存，文件路径：{}", file.getPath());
        if (!file.exists()) {
            log.error("******FileCacheServiceImpl.getNukaCode：核弹密码文件缓存不存在");
            return null;
        }
        String json = FileUtil.readUtf8String(file);
        log.debug("******FileCacheServiceImpl.getNukaCode：核弹密码文件缓存读取成功");
        return JSON.parseObject(json, NukaCode.class);
    }

    @Override
    @RegisterReflectionForBinding(NukaCode.class)
    public boolean cacheNukaCode(NukaCode nukaCode) {
        log.info("******FileCacheServiceImpl.cacheNukaCode：正在尝试写入文件");
        if (nukaCode == null) {
            log.error("******FileCacheServiceImpl.cacheNukaCode：核弹密码为空，写入失败");
            return false;
        }

        String path = PathUtil.getJarPath() + "/config/nukacode/nukaCode.json";
        FileWriter writer = new FileWriter(path);
        writer.write(JSON.toJSONString(nukaCode));
        log.info("******FileCacheServiceImpl.cacheNukaCode：写入文件成功，路径：{}", path);
        return true;
    }

    @Override
    @RegisterReflectionForBinding(PhotoInfo.class)
    public List<PhotoInfo> getPhotos() {
        File file = new File(PathUtil.getJarPath() + "/config/photo/photos.json");
        log.info("******FileCacheServiceImpl.getNukaCode：正在读取图片文件缓存，文件路径：{}", file.getPath());
        if (!file.exists()) {
            log.error("******FileCacheServiceImpl.getNukaCode：图片文件缓存不存在");
            return null;
        }
        String json = FileUtil.readUtf8String(file);
        log.debug("******FileCacheServiceImpl.getNukaCode：图片文件缓存读取成功");
        return JSONArray.parseArray(json, PhotoInfo.class);
    }

    @Override
    @RegisterReflectionForBinding(PhotoInfo.class)
    public boolean cachePhotos(List<PhotoInfo> photos) {
        log.info("******FileCacheServiceImpl.cachePhotos：正在尝试写入文件");
        if (CollUtil.isEmpty(photos)) {
            log.error("******FileCacheServiceImpl.cachePhotos：图片数据为空，写入失败");
            return false;
        }

        String path = PathUtil.getJarPath() + "/config/photo/photos.json";
        FileWriter writer = new FileWriter(path);
        writer.write(JSONArray.toJSONString(photos));
        log.info("******FileCacheServiceImpl.cachePhotos：写入文件成功，路径：{}", path);
        return true;
    }
}
