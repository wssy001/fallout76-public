package cyou.wssy001.common.service;

import cyou.wssy001.common.dto.BasePlatformEventDTO;

/**
 * @Description: 判断用户信息服务类
 * @Author: Tyler
 * @Date: 2023/3/16 16:29
 * @Version: 1.0
 */
public interface CheckUserService {

    // 传入信息，判断用户是否符合具体资格，
    // 一般用于判断用户是否为机器人在当前平台的管理员
    boolean check(BasePlatformEventDTO basePlatformEventDTO);
}
