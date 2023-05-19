package cyou.wssy001.wikiprovider.entity;


import cyou.wssy001.common.entity.BaseDBEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Effect extends BaseDBEntity {

    /**
     * 名称
     */
    private String name;

    /**
     * 值
     */
    private String value;

    /**
     * 持续时间（秒）
     */
    private Long durationSeconds;

}
