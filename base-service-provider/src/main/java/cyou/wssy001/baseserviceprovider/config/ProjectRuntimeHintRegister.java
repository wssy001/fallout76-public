package cyou.wssy001.baseserviceprovider.config;

import cyou.wssy001.common.util.PathUtil;
import cyou.wssy001.dodoadopter.aspect.DoDoHttpParamAspect;
import cyou.wssy001.kookadopter.aspect.KookHttpParamAspect;
import cyou.wssy001.qqadopter.aspect.QQHttpParamAspect;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.ProxyHints;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.boot.availability.ApplicationAvailability;
import org.springframework.context.ApplicationListener;

import java.util.List;

public class ProjectRuntimeHintRegister implements RuntimeHintsRegistrar {
    private static final List<String> DEFAULT_LOCATIONS = List.of(PathUtil.getJarPath() + "/config");

    @Override
    public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
        try {
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

            ClassLoader classLoaderToUse = (classLoader != null) ? classLoader : getClass().getClassLoader();
            String[] locations = DEFAULT_LOCATIONS.stream()
                    .filter((candidate) -> classLoaderToUse.getResource(candidate) != null)
                    .map((location) -> location + "*")
                    .toArray(String[]::new);
            if (locations.length > 0) {
                hints.resources().registerPattern((hint) -> hint.includes(locations));
            }
        } catch (Exception e) {
            throw new RuntimeException("Could not register RuntimeHint: " + e.getMessage());
        }
    }
}
