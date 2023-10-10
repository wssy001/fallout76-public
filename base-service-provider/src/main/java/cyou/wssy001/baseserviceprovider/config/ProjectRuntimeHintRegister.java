package cyou.wssy001.baseserviceprovider.config;

import cyou.wssy001.common.dto.BasePlatformEventDTO;
import cyou.wssy001.common.entity.BaseEvent;
import cyou.wssy001.common.entity.NukaCode;
import cyou.wssy001.common.entity.PhotoInfo;
import cyou.wssy001.common.enums.PlatformEnum;
import cyou.wssy001.dodoadopter.aspect.DoDoHttpParamAspect;
import cyou.wssy001.dodoadopter.dto.DoDoEventDTO;
import cyou.wssy001.dodoadopter.dto.DoDoReplyMsgDTO;
import cyou.wssy001.kookadopter.aspect.KookHttpParamAspect;
import cyou.wssy001.kookadopter.dto.KookEventDTO;
import cyou.wssy001.kookadopter.dto.KookReplyMsgDTO;
import cyou.wssy001.qqadopter.aspect.QQHttpParamAspect;
import cyou.wssy001.qqadopter.dto.QQChannelEventDTO;
import cyou.wssy001.qqadopter.dto.QQEventDTO;
import cyou.wssy001.qqadopter.dto.QQFileUploadEventDTO;
import cyou.wssy001.qqadopter.dto.QQReplyMsgDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.aot.hint.*;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.boot.availability.ApplicationAvailability;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportRuntimeHints;

import static org.springframework.aot.hint.MemberCategory.*;

@Configuration
@ImportRuntimeHints(ProjectRuntimeHintRegister.class)
public class ProjectRuntimeHintRegister implements RuntimeHintsRegistrar {

    @Override
    public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
        hints.reflection().registerType(KookHttpParamAspect.class,
                        builder -> builder.withMembers(MemberCategory.INVOKE_DECLARED_METHODS))
                .registerType(QQHttpParamAspect.class,
                        builder -> builder.withMembers(MemberCategory.INVOKE_DECLARED_METHODS))
                .registerType(DoDoHttpParamAspect.class,
                        builder -> builder.withMembers(MemberCategory.INVOKE_DECLARED_METHODS))
                .registerType(PlatformEnum.class, MemberCategory.values())
        ;

        hints.proxies().registerJdkProxy(FactoryBean.class)
                .registerJdkProxy(HttpServletRequest.class)
                .registerJdkProxy(HttpServletResponse.class)
                .registerJdkProxy(BeanClassLoaderAware.class)
                .registerJdkProxy(ApplicationListener.class)
                .registerJdkProxy(ApplicationAvailability.class)
        ;

        hints.reflection()
                .registerType(
                        TypeReference.of("com.github.benmanes.caffeine.cache.SSMSA"),
                        PUBLIC_FIELDS, INVOKE_DECLARED_CONSTRUCTORS, INVOKE_PUBLIC_METHODS)
        ;
        hints.resources()
                .registerPattern("config/**")
                .registerPattern("help/*")
                .registerPattern("nukacode/*")
                .registerPattern("dao/*.xml");

        hints.serialization()
                .registerType(NukaCode.class)
                .registerType(QQReplyMsgDTO.class)
                .registerType(PhotoInfo.class)
                .registerType(BaseEvent.class)
                .registerType(BasePlatformEventDTO.class)
                .registerType(DoDoEventDTO.class)
                .registerType(DoDoReplyMsgDTO.class)
                .registerType(QQEventDTO.class)
                .registerType(QQChannelEventDTO.class)
                .registerType(QQFileUploadEventDTO.class)
                .registerType(KookEventDTO.class)
                .registerType(KookReplyMsgDTO.class)
        ;
    }
}
