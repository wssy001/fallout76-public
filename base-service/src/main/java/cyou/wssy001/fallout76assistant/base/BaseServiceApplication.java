package cyou.wssy001.fallout76assistant.base;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @Description: 启动类
 * @Author: Tyler
 * @Date: 2023/9/11 14:41
 * @Version: 1.0
 */
@SpringBootApplication
@ComponentScan("cyou.wssy001.fallout76assistant.**")
public class BaseServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BaseServiceApplication.class, args);
    }

}
