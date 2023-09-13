package cyou.wssy001.fallout76assistant.base;

import cyou.wssy001.fallout76assistant.common.enums.EventSourceEnum;
import cyou.wssy001.fallout76assistant.common.enums.EventTypeEnum;
import cyou.wssy001.fallout76assistant.common.event.BaseEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.util.Set;

/**
 * @Description: 启动类
 * @Author: Tyler
 * @Date: 2023/9/11 14:41
 * @Version: 1.0
 */
@Slf4j
@SpringBootApplication
@ComponentScan("cyou.wssy001.fallout76assistant.**")
public class BaseServiceApplication {
    private static ApplicationContext applicationContext;


    public BaseServiceApplication(ApplicationContext applicationContext) {
        BaseServiceApplication.applicationContext = applicationContext;
    }

    public static void main(String[] args) {
        SpringApplication.run(BaseServiceApplication.class, args);

    }

}
