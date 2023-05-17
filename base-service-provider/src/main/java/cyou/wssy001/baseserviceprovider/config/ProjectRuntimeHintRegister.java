package cyou.wssy001.baseserviceprovider.config;

import cyou.wssy001.dodoadopter.aspect.DoDoHttpParamAspect;
import cyou.wssy001.kookadopter.aspect.KookHttpParamAspect;
import cyou.wssy001.qqadopter.aspect.QQHttpParamAspect;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.aot.hint.*;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.boot.availability.ApplicationAvailability;
import org.springframework.context.ApplicationListener;

import static org.springframework.aot.hint.MemberCategory.*;

public class ProjectRuntimeHintRegister implements RuntimeHintsRegistrar {

    @Override
    public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
        ProxyHints proxies = hints.proxies();
        proxies.registerJdkProxy(HttpServletRequest.class);
        proxies.registerJdkProxy(HttpServletResponse.class);
        hints.reflection().registerType(KookHttpParamAspect.class,
                builder -> builder.withMembers(MemberCategory.INVOKE_DECLARED_METHODS));
        hints.reflection().registerType(QQHttpParamAspect.class,
                builder -> builder.withMembers(MemberCategory.INVOKE_DECLARED_METHODS));
        hints.reflection().registerType(DoDoHttpParamAspect.class,
                builder -> builder.withMembers(MemberCategory.INVOKE_DECLARED_METHODS));
        hints.proxies().registerJdkProxy(FactoryBean.class, BeanClassLoaderAware.class, ApplicationListener.class);
        hints.proxies().registerJdkProxy(ApplicationAvailability.class, ApplicationListener.class);

        hints.reflection()
                .registerType(
                        TypeReference.of("com.github.benmanes.caffeine.cache.SSMSA"),
                        PUBLIC_FIELDS, INVOKE_DECLARED_CONSTRUCTORS, INVOKE_PUBLIC_METHODS)
        ;
        hints.resources()
                .registerPattern("config/**")
                .registerPattern("help/*")
                .registerPattern("nukacode/*");
    }
}
