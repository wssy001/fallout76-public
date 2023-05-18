package cyou.wssy001.qqadopter.aspect;

import cn.hutool.core.codec.Base64Encoder;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import cyou.wssy001.common.dto.BasePlatformEventDTO;
import cyou.wssy001.common.entity.BaseAdminEvent;
import cyou.wssy001.common.entity.BaseEvent;
import cyou.wssy001.common.entity.BasePrivateEvent;
import cyou.wssy001.common.enums.PlatformEnum;
import cyou.wssy001.common.service.CheckUserService;
import cyou.wssy001.common.service.DuplicateMessageService;
import cyou.wssy001.common.service.RateLimitService;
import cyou.wssy001.qqadopter.config.QQConfig;
import cyou.wssy001.qqadopter.dto.QQChannelEventDTO;
import cyou.wssy001.qqadopter.dto.QQEventDTO;
import cyou.wssy001.qqadopter.service.WeeklyOffersUpdateService;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class QQHttpParamAspect {
    private final QQConfig qqConfig;
    private final RateLimitService rateLimitService;
    private final HttpServletRequest httpServletRequest;
    private final CheckUserService<QQEventDTO> checkUserService;
    private final DuplicateMessageService duplicateMessageService;
    private final WeeklyOffersUpdateService weeklyOffersUpdateService;

    private static Mac mac;

    @PostConstruct
    public void init() throws Exception {
        String secret = qqConfig.getSecret();
        if (StrUtil.isBlank(secret)) return;

        SecretKeySpec keySpec = new SecretKeySpec(secret.getBytes(), "HmacSHA1");
        mac = Mac.getInstance("HmacSHA1");
        mac.init(keySpec);
    }

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
        String verifySign = httpServletRequest.getHeader("X-Signature");
        if (StrUtil.isNotBlank(verifySign)) {
            log.debug("******QQHttpParamAspect.checkQQHttpParam：传入的 X-Signature：{}", verifySign);
            if (!verifyBody(verifySign, body)) {
                log.error("******QQHttpParamAspect.checkQQHttpParam：签名验证失败，暂不处理请求：{}", new String(body));
                return null;
            }
        }

        JSONObject jsonObject = JSON.parseObject(body);
        log.info("******QQHttpParamAspect.checkQQHttpParam：收到请求：{}", jsonObject.toJSONString());

        // 更新日替
        if (jsonObject.getString("post_type").equals("notice") &&
                jsonObject.getString("notice_type").equals("group_upload") &&
                jsonObject.getLong("group_id").equals(733491495L) &&
                jsonObject.getLong("user_id").equals(1137631718L)) {
            weeklyOffersUpdateService.updateWeeklyOffers(jsonObject);
            return null;
        }

        String message = jsonObject.getString("message");
        String key = ReUtil.getGroup0("^/[一-龥a-zA-z]+", message);
        if (StrUtil.isBlank(key)) return null;

        BasePlatformEventDTO basePlatformEventDTO;
        BaseEvent baseEvent;
        String messageType = jsonObject.getString("message_type");
        String messageId = jsonObject.getString("message_id");
        if (StrUtil.hasBlank(messageType, messageId)) return null;

        PlatformEnum platform = messageType.equals("guild") ? PlatformEnum.QQ_GUILD : PlatformEnum.QQ;
        if (duplicateMessageService.hasConsumed(messageId, platform)) {
            log.debug("******QQHttpParamAspect.checkQQHttpParam：消息：{} 已被消费", jsonObject.toJSONString());
            return null;
        }

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
                if (checkUserService.check((QQEventDTO) basePlatformEventDTO)) {
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

        String userId = jsonObject.getString("user_id");
        if (!rateLimitService.hasRemain(userId, key, platform)) {
            log.error("******QQHttpParamAspect.checkQQHttpParam：发现 {} 用户：{} 重复请求指令：{}", platform.getDescription(), userId, key);
            return null;
        }

        return joinPoint.proceed(new Object[]{baseEvent, basePlatformEventDTO});
    }

    private boolean verifyBody(String verifySign, byte[] body) {
        if (mac == null) {
            log.error("******QQHttpParamAspect.verifyBody：无法获取HmacSHA1密钥，请先配置robot-config.qq.secret");
            return false;
        }
        if (verifySign.contains("sha1")) verifySign = verifySign.substring(5);

        byte[] hex = mac.doFinal(body);
        String sign = HexUtil.encodeHexStr(hex);
        return verifySign.equals(sign);
    }

}
