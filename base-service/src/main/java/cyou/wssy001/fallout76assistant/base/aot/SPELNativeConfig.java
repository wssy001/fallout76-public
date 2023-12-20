package cyou.wssy001.fallout76assistant.base.aot;

import cyou.wssy001.fallout76assistant.common.service.BaseEventListener;
import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.aot.hint.TypeReference;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportRuntimeHints;

@Configuration
@ImportRuntimeHints(SPELNativeConfig.class)
public class SPELNativeConfig implements RuntimeHintsRegistrar {

    @Override
    public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
        hints.reflection()
                .registerType(TypeReference.of("org.springframework.context.event.EventExpressionRootObject"),
                        MemberCategory.values())
        ;

        hints.reflection()
                .registerType(BaseEventListener.class, MemberCategory.values())
        ;
    }
}
