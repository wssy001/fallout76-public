package cyou.wssy001.qqadopter.aspect;

import cn.hutool.core.codec.Base64Encoder;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.HMac;
import cn.hutool.crypto.digest.HmacAlgorithm;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import cyou.wssy001.common.dto.BasePlatformEventDTO;
import cyou.wssy001.common.entity.BaseAdminEvent;
import cyou.wssy001.common.entity.BaseEvent;
import cyou.wssy001.common.entity.BasePrivateEvent;
import cyou.wssy001.common.enums.PlatformEnum;
import cyou.wssy001.common.service.CheckUser;
import cyou.wssy001.qqadopter.config.QQConfig;
import cyou.wssy001.qqadopter.dto.QQChannelEventDTO;
import cyou.wssy001.qqadopter.dto.QQEventDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class QQHttpParamAspect {
    private final QQConfig qqConfig;
    private final CheckUser<QQEventDTO> checkUser;
    private final HttpServletRequest httpServletRequest;


    @Pointcut("execution(void cyou.wssy001.qqadopter.controller.GoCQHttpEventController.handleQQEvent(..))")
    public void pointcut() {
    }

    @Around("pointcut()")
    @RegisterReflectionForBinding({QQEventDTO.class, QQChannelEventDTO.class})
    public Object checkQQHttpParam(ProceedingJoinPoint joinPoint) throws Throwable {
        if (!qqConfig.isEnableQQ() && !qqConfig.isEnableQQChannel()) return null;

        byte[] body = httpServletRequest.getInputStream()
                .readAllBytes();
        log.debug("******QQHttpParamAspect.checkQQHttpParam：传入的数据 BASE64：{}", Base64Encoder.encode(body));
        String secret = qqConfig.getSecret();
        String verifySign = httpServletRequest.getHeader("X-Signature");
        if (StrUtil.isNotBlank(verifySign)) {
            if (!verifyBody(verifySign, body, secret)) {
                log.error("******QQHttpParamAspect.checkQQHttpParam：签名验证失败，暂不处理请求：{}", new String(body));
                return null;
            }
        }

        JSONObject jsonObject = JSON.parseObject(body);
        log.info("******QQHttpParamAspect.checkQQHttpParam：收到请求：{}", jsonObject.toJSONString());
        String message = jsonObject.getString("message");
        String key = ReUtil.getGroup0("^/[一-龥a-zA-z]+", message);
        if (StrUtil.isBlank(key)) return null;

        BasePlatformEventDTO basePlatformEventDTO;
        BaseEvent baseEvent;
        String messageType = jsonObject.getString("message_type");
        if (StrUtil.isBlank(messageType)) return null;
        switch (messageType) {
            case "guild" -> {
                if (!qqConfig.isEnableQQChannel()) return null;

                baseEvent = new BaseEvent()
                        .setEventKey(key)
                        .setPlatform(PlatformEnum.QQ_GUILD);
                basePlatformEventDTO = jsonObject.toJavaObject(QQChannelEventDTO.class);
            }
            case "group" -> {
                if (!qqConfig.isEnableQQ()) return null;

                baseEvent = new BaseEvent()
                        .setEventKey(key)
                        .setPlatform(PlatformEnum.QQ);
                basePlatformEventDTO = jsonObject.toJavaObject(QQEventDTO.class);
            }
            case "private" -> {
                if (!qqConfig.isEnableQQ()) return null;

                basePlatformEventDTO = jsonObject.toJavaObject(QQEventDTO.class);
                if (checkUser.check((QQEventDTO) basePlatformEventDTO)) {
                    baseEvent = new BaseAdminEvent()
                            .setEventKey(key)
                            .setPlatform(PlatformEnum.QQ);
                } else {
                    baseEvent = new BasePrivateEvent()
                            .setEventKey(key)
                            .setPlatform(PlatformEnum.QQ);
                }
            }
            default -> {
                baseEvent = null;
                basePlatformEventDTO = null;
            }
        }

        if (ObjUtil.hasNull(baseEvent, basePlatformEventDTO)) return null;
        return joinPoint.proceed(new Object[]{baseEvent, basePlatformEventDTO});
    }

    private boolean verifyBody(String verifySign, byte[] body, String key) {
        if (StrUtil.isBlank(key)) {
            log.error("******QQHttpParamAspect.verifyBody：无法获取HmacSHA1密钥，请先配置robot-config.qq.secret");
            return false;
        }
        if (verifySign.contains("sha1")) verifySign = verifySign.substring(5);
        HMac mac = new HMac(HmacAlgorithm.HmacSHA1, key.getBytes());
        String sign = mac.digestHex(body);
        return verifySign.equals(sign);
    }

}
