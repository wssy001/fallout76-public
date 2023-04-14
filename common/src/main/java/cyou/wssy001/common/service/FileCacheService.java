package cyou.wssy001.common.service;

import cyou.wssy001.common.entity.NukaCode;

/**
 * @Description: 文件缓存服务类
 * @Author: Tyler
 * @Date: 2023/3/17 20:21
 * @Version: 1.0
 */
public interface FileCacheService {

    // 从文件中读取核弹密码缓存
    NukaCode getNukaCode();

    // 将核弹密码覆盖写入文件进行缓存
    boolean cacheNukaCode(NukaCode nukaCode);
}
