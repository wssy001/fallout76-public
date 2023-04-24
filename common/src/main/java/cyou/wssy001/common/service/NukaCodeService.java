package cyou.wssy001.common.service;

import cyou.wssy001.common.entity.NukaCode;

/**
 * @Description: 核弹密码服务类
 * @Author: Tyler
 * @Date: 2023/3/16 10:19
 * @Version: 1.0
 */
public interface NukaCodeService {

    // 从网站获取信息以刷新核弹密码
    boolean refreshNukaCode(boolean cache);

    // 更新内存中的核弹密码信息
    boolean updateNukaCode(NukaCode nukaCode);

    // 从内存中读取核弹密码信息
    NukaCode getNukaCode();
}
