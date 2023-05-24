package cyou.wssy001.common.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FoodDTO {
    private String name;

    private String englishName;

    private Integer rads;

    private int diseaseChance;

    private Integer food;

    private float weight;

    private String imageName;

    private Integer healthRestored;

    private Integer value;

    private boolean addiction;

    private boolean plantable;

    private List<EffectDTO> effects;
}
