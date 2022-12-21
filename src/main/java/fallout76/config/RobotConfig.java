package fallout76.config;

import io.quarkus.runtime.annotations.StaticInitSafe;
import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;
import io.smallrye.config.WithName;

import java.util.List;
import java.util.Optional;

@StaticInitSafe
@ConfigMapping(prefix = "robot-config")
public interface RobotConfig {

    @WithDefault("")
    @WithName("go-cqhttp-url")
    Optional<String> goCQHttpUrl();

    @WithDefault("false")
    @WithName("enable-qq")
    Optional<Boolean> enableQQ();

    @WithDefault("false")
    @WithName("enable-qq-channel")
    Optional<Boolean> enableQQChannel();

    @WithDefault("false")
    Optional<Boolean> enableKook();

    Kook kook();

    List<String> admins();

    interface Kook {
        @WithDefault("")
        Optional<String> openApiBaseUrl();

        @WithDefault("")
        Optional<String> token();

        @WithDefault("")
        Optional<String> verifyToken();

        @WithDefault("")
        Optional<String> encryptKey();

        @WithDefault("http://bot.gekj.net")
        Optional<String> botMarketOnlineApiUrl();

        @WithDefault("")
        @WithName("bot-market-uuid")
        Optional<String> botMarketUUID();
    }
}
