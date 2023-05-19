package cyou.wssy001.wikiprovider.dao;

import cyou.wssy001.wikiprovider.entity.Food;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FoodDAO {

    List<Food> list(@Param("name") String name, @Param("english") boolean english);

    Food selectByPrimaryKey(@Param("id") Long id);
}
