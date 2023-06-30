package cyou.wssy001.common.service;

import cyou.wssy001.common.entity.NukaCode;
import cyou.wssy001.common.entity.PhotoInfo;
import cyou.wssy001.common.enums.PlatformEnum;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Description: 图片服务类
 * @Author: Tyler
 * @Date: 2023/3/17 20:36
 * @Version: 1.0
 */
public interface PhotoService {

    // 通过图片名获取图片URL （不带.jpg等后缀名）
    Map<String, String> getPhotoUrls(String name, PlatformEnum platform);

    // 获取所有图片信息（Key为图片名，不带.jpg等后缀名，Value为图片URL）
    List<PhotoInfo> getPhotos();

    boolean refreshPhotos(boolean cache);

    void createNukaCodePhoto(String name, NukaCode nukaCode);

    void createHelpPhoto(String name, HashMap<Set<String>, String> map);

    boolean updatePhotoUrl(PhotoInfo photoInfo);

    boolean storePhotoCache();
}
