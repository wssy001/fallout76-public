package cyou.wssy001.fallout76assistant.kookadapter.config;

import cyou.wssy001.fallout76assistant.kookadapter.event.KookEvent;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportRuntimeHints;

@Configuration
@ImportRuntimeHints(KookAdapterNativeConfig.class)
public class KookAdapterNativeConfig implements RuntimeHintsRegistrar {

    @Override
    public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
        hints.serialization()
                .registerType(KookEvent.class)
        ;
    }
}
