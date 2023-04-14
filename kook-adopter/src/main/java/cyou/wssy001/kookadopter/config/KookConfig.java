package cyou.wssy001.kookadopter.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

/**
 * @Description: Kook配置类，参数详见application.yml
 * @Author: Tyler
 * @Date: 2023/3/16 09:17
 * @Version: 1.0
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "robot-config.kook")
public class KookConfig {

    private boolean enable;

    private String openApiBaseUrl;

    private String token;

    private String verifyToken;

    private String encryptKey;

    private String botMarketOnlineApiUrl;

    private String botMarketUUID;

    private Set<String> admins;

}
