package cyou.wssy001.wikiprovider.convert;

import cyou.wssy001.common.dto.FoodDTO;
import cyou.wssy001.wikiprovider.entity.Food;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FoodConvert {

    FoodDTO toDTO(Food food);

    List<FoodDTO> toDTOList(List<Food> foodList);
}
