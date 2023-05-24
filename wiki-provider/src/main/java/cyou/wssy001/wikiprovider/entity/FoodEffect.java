package cyou.wssy001.wikiprovider.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class FoodEffect {

    private Long foodId;

    private Long effectId;

}
