package cyou.wssy001.baseserviceprovider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("cyou.wssy001.**")
public class BaseServiceProviderApplication {
    public static void main(String[] args) {
        SpringApplication.run(BaseServiceProviderApplication.class, args);
    }
}
