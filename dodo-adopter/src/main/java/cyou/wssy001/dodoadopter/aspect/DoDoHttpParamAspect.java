package cyou.wssy001.dodoadopter.aspect;

import cn.hutool.core.codec.Base64Encoder;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import cyou.wssy001.common.entity.BaseAdminEvent;
import cyou.wssy001.common.entity.BaseEvent;
import cyou.wssy001.common.entity.BasePrivateEvent;
import cyou.wssy001.common.enums.EventEnum;
import cyou.wssy001.common.enums.PlatformEnum;
import cyou.wssy001.common.service.CheckUserService;
import cyou.wssy001.common.service.DuplicateMessageService;
import cyou.wssy001.common.service.RateLimitService;
import cyou.wssy001.dodoadopter.config.DoDoConfig;
import cyou.wssy001.dodoadopter.dto.DoDoEventDTO;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.AlgorithmParameters;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class DoDoHttpParamAspect {
    private final DoDoConfig dodoConfig;
    private final CheckUserService checkDoDoAdminService;
    private final RateLimitService rateLimitService;
    private final HttpServletRequest httpServletRequest;
    private final HttpServletResponse httpServletResponse;
    private final DuplicateMessageService duplicateMessageService;

    private static Cipher cipher;


    @PostConstruct
    public void init() throws Exception {
        String secretKey = dodoConfig.getSecretKey();
        if (StrUtil.isBlank(secretKey)) return;

        byte[] keyBytes = HexUtil.decodeHex(secretKey);
        SecretKeySpec sKeySpec = new SecretKeySpec(keyBytes, "AES");
        AlgorithmParameters params = AlgorithmParameters.getInstance("AES");
        params.init(new IvParameterSpec(new byte[16]));
        cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
        cipher.init(Cipher.DECRYPT_MODE, sKeySpec, params);
    }

    @Pointcut("execution(void cyou.wssy001.dodoadopter.controller.DoDoHttpEventController.handleDoDoEvent(..))")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object checkDoDoHttpParam(ProceedingJoinPoint joinPoint) throws Throwable {
        if (!dodoConfig.isEnable()) return null;
        if (cipher == null) {
            log.error("******DoDoHttpParamAspect.checkDoDoHttpParam：事件密钥不能为空，请先配置robot-config.dodo.secret-key");
            return null;
        }

        byte[] body = httpServletRequest.getInputStream()
                .readAllBytes();
        if (body.length == 0) return null;

        log.debug("******DoDoHttpParamAspect.checkDoDoHttpParam：传入的数据 BASE64：{}", Base64Encoder.encode(body));
        JSONObject jsonObject = JSON.parseObject(body);
        String clientId = dodoConfig.getClientId();
        if (StrUtil.isBlank(clientId)) {
            log.error("******DoDoHttpParamAspect.checkDoDoHttpParam：应用唯一标识不能为空，请先配置robot-config.dodo.client-id");
            return null;
        }

        String payload = jsonObject.getString("payload");
        String decrypt = decrypt(payload);
        log.debug("******DoDoHttpParamAspect.checkDoDoHttpParam：解密后的数据：{}", decrypt);
        DoDoEventDTO dodoEventDTO = JSON.parseObject(decrypt, DoDoEventDTO.class);
        int type = dodoEventDTO.getType();
        if (type == 2) {
            jsonObject = JSON.parseObject(decrypt);
            jsonObject.put("status", 0);
            jsonObject.put("message", "");
            httpServletResponse.getWriter()
                    .write(decrypt);
            return null;
        }
        if (type != 0) return null;

        DoDoEventDTO.EventBody dodoEventDTOData = dodoEventDTO.getData();
        JSONObject eventBody = dodoEventDTOData.getEventBody();
        String content = eventBody.getJSONObject("messageBody")
                .getString("content");
        String key = ReUtil.getGroup0("^/[一-龥a-zA-z]+", content);
        if (StrUtil.isBlank(key)) return null;

        String messageId = eventBody.getString("messageId");
        if (StrUtil.isBlank(messageId)) {
            log.error("******DoDoHttpParamAspect.checkDoDoHttpParam：messageId为空");
            return null;
        }

        if (duplicateMessageService.hasConsumed(messageId, PlatformEnum.DODO)) {
            log.debug("******DoDoHttpParamAspect.checkDoDoHttpParam：消息：{} 已被消费", jsonObject.toJSONString());
            return null;
        }

        String dodoSourceId = eventBody.getString("dodoSourceId");
        BaseEvent baseEvent;
        switch (dodoEventDTOData.getEventType()) {
//            消息事件
            case "2001" -> {
                if (!rateLimitService.hasRemain("channel/message/send", PlatformEnum.DODO, EventEnum.GROUP)) {
                    log.error("******DoDoHttpParamAspect.checkDoDoHttpParam：无法回复 {} 用户：{} 的指令：{}，API限速中", PlatformEnum.DODO.getDescription(), dodoSourceId, key);
                    return null;
                }

                baseEvent = new BaseEvent()
                        .setEventKey(key)
                        .setPlatform(PlatformEnum.DODO)
                        .setEventType(EventEnum.GROUP);
            }
//            私信事件
            case "1001" -> {
                if (!rateLimitService.hasRemain("personal/message/send", PlatformEnum.DODO, EventEnum.FRIEND)) {
                    log.error("******DoDoHttpParamAspect.checkDoDoHttpParam：无法回复 {} 用户：{} 的指令：{}，API限速中", PlatformEnum.DODO.getDescription(), dodoSourceId, key);
                    return null;
                }

                if (checkDoDoAdminService.check(dodoEventDTO)) {
                    baseEvent = new BaseAdminEvent()
                            .setEventKey(key)
                            .setPlatform(PlatformEnum.DODO)
                            .setEventType(EventEnum.ADMIN);
                } else {
                    baseEvent = new BasePrivateEvent()
                            .setEventKey(key)
                            .setPlatform(PlatformEnum.DODO)
                            .setEventType(EventEnum.FRIEND);
                }

            }
            default -> {
                return null;
            }
        }

        if (!rateLimitService.hasRemain(dodoSourceId, key, PlatformEnum.DODO)) {
            log.error("******DoDoHttpParamAspect.checkDoDoHttpParam：发现 {} 用户：{} 重复请求指令：{}", PlatformEnum.DODO.getDescription(), dodoSourceId, key);
            return null;
        }

        return joinPoint.proceed(new Object[]{baseEvent, dodoEventDTO});
    }

    private String decrypt(String encrypt) throws Exception {
        byte[] bytes = HexUtil.decodeHex(encrypt);
        byte[] bytes1 = cipher.doFinal(bytes);
        return new String(bytes1);
    }

}
