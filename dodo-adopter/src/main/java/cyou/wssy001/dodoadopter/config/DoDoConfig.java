package cyou.wssy001.dodoadopter.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

/**
 * @Description: DoDo配置类，参数详见application.yml
 * @Author: Tyler
 * @Date: 2023/3/16 09:17
 * @Version: 1.0
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "robot-config.dodo")
public class DoDoConfig {
    private boolean enable;
    private String clientId;
    private String botToken;
    private String secretKey;
    private String openApiBaseUrl;
    private Set<String> admins;
}
