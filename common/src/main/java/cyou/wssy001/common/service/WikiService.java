package cyou.wssy001.common.service;

import cyou.wssy001.common.dto.FoodDTO;

import java.util.List;

/**
 * @Description: Wiki服务类
 * @Author: Tyler
 * @Date: 2023/5/15 10:24
 * @Version: 1.0
 */
public interface WikiService {

    List<FoodDTO> searchFood(String name);
}
