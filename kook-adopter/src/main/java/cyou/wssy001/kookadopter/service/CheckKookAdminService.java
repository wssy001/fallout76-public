package cyou.wssy001.kookadopter.service;

import cyou.wssy001.common.dto.BasePlatformEventDTO;
import cyou.wssy001.common.service.CheckUserService;
import cyou.wssy001.kookadopter.config.KookConfig;
import cyou.wssy001.kookadopter.dto.KookEventDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @Description: 判断用户是否为机器人在Kook的管理员
 * @Author: Tyler
 * @Date: 2023/3/16 16:27
 * @Version: 1.0
 */
@Service
@RequiredArgsConstructor
public class CheckKookAdminService implements CheckUserService {
    private final KookConfig kookConfig;


    @Override
    public boolean check(BasePlatformEventDTO basePlatformEventDTO) {
        if (basePlatformEventDTO instanceof KookEventDTO kookEventDTO) {
            String authorId = kookEventDTO.getAuthorId();
            return kookConfig.getAdmins()
                    .contains(authorId);
        }
        return false;
    }
}
