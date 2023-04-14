package cyou.wssy001.common.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Description: 核弹密码对象类
 * @Author: Tyler
 * @Date: 2023/3/15 14:23
 * @Version: 1.0
 */
@Getter
@Setter
@Accessors(chain = true)
public class NukaCode {

    //    A点
    private String alpha;

    //    B点
    private String bravo;

    //    C点
    private String charlie;

    //    过期时间（北京时间每周日8时过期）
    private LocalDateTime expireTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NukaCode nukaCode = (NukaCode) o;

        if (!alpha.equals(nukaCode.alpha)) return false;
        if (!bravo.equals(nukaCode.bravo)) return false;
        if (!charlie.equals(nukaCode.charlie)) return false;
        return expireTime.equals(nukaCode.expireTime);
    }

    @Override
    public int hashCode() {
        int result = alpha.hashCode();
        result = 31 * result + bravo.hashCode();
        result = 31 * result + charlie.hashCode();
        result = 31 * result + expireTime.hashCode();
        return result;
    }
}
