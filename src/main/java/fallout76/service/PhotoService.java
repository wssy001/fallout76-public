package fallout76.service;

import cn.hutool.core.util.StrUtil;
import fallout76.config.RobotConfig;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PhotoService {
    @Inject
    RobotConfig robotConfig;

    public String getPhoto(String name) {
        if (StrUtil.isBlank(name)) return null;
        return robotConfig.photos()
                .get(name);
    }
}
