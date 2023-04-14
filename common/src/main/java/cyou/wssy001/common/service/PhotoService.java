package cyou.wssy001.common.service;

import java.util.Map;

/**
 * @Description: 图片服务类
 * @Author: Tyler
 * @Date: 2023/3/17 20:36
 * @Version: 1.0
 */
public interface PhotoService {

    // 通过图片名获取图片URL （不带.jpg等后缀名）
    String getPhotoUrl(String photoName);

    // 获取所有图片信息（Key为图片名，不带.jpg等后缀名，Value为图片URL）
    // map中含有一个Key（serviceProvider）指明当前服务提供类
    Map<String, String> getPhotos();
}
