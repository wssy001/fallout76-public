package cyou.wssy001.fallout76assistant.common.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;

import java.io.File;
import java.net.URL;

/**
 * @Description: 路径工具类
 * @Author: Tyler
 * @Date: 2023/3/16 21:24
 * @Version: 1.0
 */
public class PathUtil {
    /**
     * 获取可执行文件所在目录路径
     * @return String 路径
     */
    public static String getJarPath() {
        URL url = PathUtil.class.getProtectionDomain().getCodeSource().getLocation();
        File file = new File(url.getPath());

        //Native Image Mode
        if (System.getProperty("org.graalvm.nativeimage.imagecode") != null) {
            file = FileUtil.getParent(file, 1);
            return file.getPath();
        }

        String path = url.getPath();
        String group0 = ReUtil.getGroup0("/base-service-provider-(.*).jar", path);

        //Java -jar Mode
        if (StrUtil.isNotBlank(group0)) {
            path = path.substring(0, path.lastIndexOf(group0));
            return path;
        }

        //IDEA Dev Mode
        file = new File(path);
        return FileUtil.getParent(file, 3)
                .getPath();
    }
}
