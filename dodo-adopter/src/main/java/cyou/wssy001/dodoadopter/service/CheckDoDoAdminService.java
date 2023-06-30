package cyou.wssy001.dodoadopter.service;

import com.alibaba.fastjson2.JSONObject;
import cyou.wssy001.common.dto.BasePlatformEventDTO;
import cyou.wssy001.common.service.CheckUserService;
import cyou.wssy001.dodoadopter.config.DoDoConfig;
import cyou.wssy001.dodoadopter.dto.DoDoEventDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @Description: 判断用户是否为机器人在DoDo的管理员
 * @Author: Tyler
 * @Date: 2023/3/16 16:27
 * @Version: 1.0
 */
@Service
@RequiredArgsConstructor
public class CheckDoDoAdminService implements CheckUserService {
    private final DoDoConfig DoDoConfig;


    @Override
    public boolean check(BasePlatformEventDTO basePlatformEventDTO) {
        if (basePlatformEventDTO instanceof DoDoEventDTO dodoEventDTO) {
            DoDoEventDTO.EventBody dodoEventDTOData = dodoEventDTO.getData();
            JSONObject eventBody = dodoEventDTOData.getEventBody();
            String dodoSourceId = eventBody.getString("dodoSourceId");
            return DoDoConfig.getAdmins()
                    .contains(dodoSourceId);
        }
        return false;
    }
}
