package cyou.wssy001.wikiprovider.service;

import cn.hutool.core.util.ReUtil;
import cyou.wssy001.common.dto.FoodDTO;
import cyou.wssy001.common.service.WikiService;
import cyou.wssy001.wikiprovider.convert.FoodConvert;
import cyou.wssy001.wikiprovider.dao.FoodDAO;
import cyou.wssy001.wikiprovider.entity.Food;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: Wiki服务实现类
 * @Author: Tyler
 * @Date: 2023/5/15 10:24
 * @Version: 1.0
 */
@Service
@RequiredArgsConstructor
public class WikiServiceImpl implements WikiService {
    private final FoodDAO foodDAO;
    private final FoodConvert foodConvert;


    @Override
    public List<FoodDTO> searchFood(String name) {
        int length = ReUtil.getGroup0("[a-zA-Z]", name).length();
        boolean english = length > (name.length() / 2);
        List<Food> foodList = foodDAO.list(name, english);
        return foodConvert.toDTOList(foodList);
    }
}
