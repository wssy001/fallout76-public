package cyou.wssy001.common.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BaseConsumable extends BaseDBEntity {
    private String name;

    private String englishName;

    private Integer rads;

    private int diseaseChance;

    private Integer food;

    private Integer water;

    private float weight;

    private String imageName;

}
