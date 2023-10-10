package cyou.wssy001.common.entity;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Description: 核弹密码对象类
 * @Author: Tyler
 * @Date: 2023/3/15 14:23
 * @Version: 1.0
 */
@Getter
@Setter
@Accessors(chain = true)
public class NukaCode implements Serializable {

    //    A点
    private String alpha;

    //    B点
    private String bravo;

    //    C点
    private String charlie;

    //    开始时间
    @JSONField(name = "start_time")
    private LocalDateTime startTime;

    //    过期时间（北京时间每周日8时过期）
    @JSONField(name = "expire_time")
    private LocalDateTime expireTime;
}
