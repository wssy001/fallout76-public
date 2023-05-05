package cyou.wssy001.qqadopter.service;

import cyou.wssy001.common.service.CheckUser;
import cyou.wssy001.qqadopter.config.QQConfig;
import cyou.wssy001.qqadopter.dto.QQEventDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @Description: 判断用户是否为机器人在QQ的管理员
 * @Author: Tyler
 * @Date: 2023/3/16 16:27
 * @Version: 1.0
 */
@RequiredArgsConstructor
@Service
public class CheckQQAdminService implements CheckUser<QQEventDTO> {
    private final QQConfig QQConfig;


    @Override
    public boolean check(QQEventDTO qqEventDTO) {
        Long userId = qqEventDTO.getUserId();
        return QQConfig.getAdmins()
                .contains(userId);
    }
}
