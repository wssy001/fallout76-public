package fallout76.util;

import cn.hutool.core.io.FileUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.net.URL;

@Slf4j
public class PathUtil {

    public static String getJarPath() {
        URL url = PathUtil.class.getProtectionDomain().getCodeSource().getLocation();
        File file = new File(url.getPath());
        file = FileUtil.getParent(file, 1);

        if (System.getProperty("org.graalvm.nativeimage.kind") != null) {
            file = FileUtil.getParent(file, 0);
        }
        return file.getPath();
    }
}
