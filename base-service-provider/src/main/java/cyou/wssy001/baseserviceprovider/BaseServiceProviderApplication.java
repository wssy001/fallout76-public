package cyou.wssy001.baseserviceprovider;

import cyou.wssy001.baseserviceprovider.config.ProjectRuntimeHintRegister;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.ImportRuntimeHints;

@SpringBootApplication
@EnableAspectJAutoProxy
@ComponentScan("cyou.wssy001.**")
@ImportRuntimeHints(value = {ProjectRuntimeHintRegister.class})
public class BaseServiceProviderApplication {
    public static void main(String[] args) {
        SpringApplication.run(BaseServiceProviderApplication.class, args);
    }
}
