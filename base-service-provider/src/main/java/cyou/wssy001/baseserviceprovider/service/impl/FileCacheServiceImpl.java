package cyou.wssy001.baseserviceprovider.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileWriter;
import com.alibaba.fastjson2.JSON;
import cyou.wssy001.common.entity.NukaCode;
import cyou.wssy001.common.service.FileCacheService;
import cyou.wssy001.common.util.PathUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * @Description: 文件缓存服务实现类
 * @Author: Tyler
 * @Date: 2023/3/17 20:16
 * @Version: 1.0
 */
@Slf4j
@Service
public class FileCacheServiceImpl implements FileCacheService {

    @Override
    @RegisterReflectionForBinding(NukaCode.class)
    public NukaCode getNukaCode() {
        File file = new File(PathUtil.getJarPath() + "/config/nukaCode.json");
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

        String path = PathUtil.getJarPath() + "/config/nukaCode.json";
        FileWriter writer = new FileWriter(path);
        writer.write(JSON.toJSONString(nukaCode));
        log.info("******FileCacheServiceImpl.cacheNukaCode：写入文件成功，路径：{}", path);
        return true;
    }
}
