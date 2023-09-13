package cyou.wssy001.fallout76assistant.common.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @Description:
 * @Author: Tyler
 * @Date: 2023/9/11 15:47
 * @Version: 1.0
 */
@Getter
@Setter
@Accessors(chain = true)
public class TestDTO {
    private long orderId;
    private LocalDateTime createTime;
}
