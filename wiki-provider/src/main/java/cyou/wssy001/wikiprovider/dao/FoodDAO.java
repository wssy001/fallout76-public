package cyou.wssy001.wikiprovider.dao;

import cyou.wssy001.wikiprovider.entity.Food;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface FoodDAO {

    @Select("""
            SELECT
            	f.id,
            	f.`name`,
            	f.english_name,
            	f.rads,
            	f.disease_chance,
            	f.food,
            	f.water,
            	f.weight,
            	f.health_restored,
            	f.`value`,
            	f.addiction,
            	f.plantable,
            	e.id AS e_id,
            	e.`name` AS e_name,
            	e.`value` AS e_value,
            	e.duration_seconds AS e_duration_seconds
            FROM
            	food AS f
            	INNER JOIN food_effect AS fe ON f.id = fe.food_id
            	INNER JOIN effect AS e ON fe.effect_id = e.id
            WHERE
            	f.english_name LIKE #{englishName}
            	LIMIT 3
            """)
    List<Food> listByEnglishName(@Param("englishName") String englishName);

    @Select("""
            SELECT
            	f.id,
            	f.`name`,
            	f.english_name,
            	f.rads,
            	f.disease_chance,
            	f.food,
            	f.water,
            	f.weight,
            	f.health_restored,
            	f.`value`,
            	f.addiction,
            	f.plantable,
            	e.id AS e_id,
            	e.`name` AS e_name,
            	e.`value` AS e_value,
            	e.duration_seconds AS e_duration_seconds
            FROM
            	food AS f
            	INNER JOIN food_effect AS fe ON f.id = fe.food_id
            	INNER JOIN effect AS e ON fe.effect_id = e.id
            WHERE
            	f.`name` LIKE #{name}
            	LIMIT 3
            """)
    List<Food> listByName(@Param("name") String name);

    @Select("""
            SELECT
            	id,
            	`name`,
            	english_name,
            	rads,
            	disease_chance,
            	food,
            	water,
            	weight,
            	health_restored,
            	`value`,
            	addiction,
            	plantable
            FROM
            	food
            WHERE
            	id = #{id}
            """)
    Food selectByPrimaryKey(@Param("id") Long id);
}
