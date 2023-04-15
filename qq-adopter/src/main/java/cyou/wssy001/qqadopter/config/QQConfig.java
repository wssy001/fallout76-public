package cyou.wssy001.qqadopter.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

/**
 * @Description: QQ配置类，参数详见application.yml
 * @Author: Tyler
 * @Date: 2023/3/16 09:17
 * @Version: 1.0
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "robot-config.qq")
public class QQConfig {

    private boolean enableQQ;

    private boolean enableQQChannel;

    private String goCqhttpUrl;

    private String accessToken;

    private String secret;

    private Set<Long> admins;

    private Set<String> channelAdmins;

}
