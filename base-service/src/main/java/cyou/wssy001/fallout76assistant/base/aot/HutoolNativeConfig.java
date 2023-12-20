package cyou.wssy001.fallout76assistant.base.aot;

import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.aot.hint.TypeReference;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportRuntimeHints;

/**
 * @Description:
 * @Author: Tyler
 * @Date: 2023/9/13 10:47
 * @Version: 1.0
 */
@Configuration
@ImportRuntimeHints(HutoolNativeConfig.class)
public class HutoolNativeConfig implements RuntimeHintsRegistrar {
    @Override
    public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
        hints.reflection()
                .registerType(TypeReference.of("cn.hutool.core.lang.Snowflake"), MemberCategory.values());
    }
}
