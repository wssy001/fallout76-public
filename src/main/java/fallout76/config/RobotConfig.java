package fallout76.config;

import io.quarkus.runtime.annotations.StaticInitSafe;
import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;
import io.smallrye.config.WithName;

import java.util.Map;
import java.util.Optional;

@StaticInitSafe
@ConfigMapping(prefix = "robot-config")
public interface RobotConfig {

    @WithDefault("")
    Optional<String> cqGoHttpUrl();

    @WithDefault("false")
    @WithName("enable-qq")
    Optional<Boolean> enableQQ();

    @WithDefault("false")
    @WithName("enable-qq-channel")
    Optional<Boolean> enableQQChannel();

    @WithDefault("false")
    Optional<Boolean> enableKook();


    Kook kook();

    Map<String, String> photos();

    interface Kook {

        @WithDefault("")
        Optional<String> token();

        @WithDefault("")
        Optional<String> verifyToken();

        @WithDefault("")
        Optional<String> encryptKey();
    }
}
