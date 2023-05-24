package cyou.wssy001.wikiprovider.entity;

import cyou.wssy001.common.entity.BaseConsumable;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public class Food extends BaseConsumable {

    /**
     * 生命回复
     */
    private Integer healthRestored;

    /**
     * 价值
     */
    private Integer value;

    /**
     * 是否成瘾
     */
    private boolean addiction;

    /**
     * 是否可种植
     */
    private boolean plantable;

    private List<Effect> effects;
}
