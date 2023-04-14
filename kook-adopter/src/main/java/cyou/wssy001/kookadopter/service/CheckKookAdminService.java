package cyou.wssy001.kookadopter.service;

import cyou.wssy001.common.service.CheckUser;
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
@RequiredArgsConstructor
@Service("checkKookAdmin")
public class CheckKookAdminService implements CheckUser<KookEventDTO> {
    private final KookConfig kookConfig;


    @Override
    public boolean check(KookEventDTO kookEventDTO) {
        String authorId = kookEventDTO.getAuthorId();
        return kookConfig.getAdmins()
                .contains(authorId);
    }
}
