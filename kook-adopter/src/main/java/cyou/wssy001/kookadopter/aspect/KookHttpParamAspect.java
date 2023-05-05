package cyou.wssy001.kookadopter.aspect;

import cn.hutool.core.codec.Base64Encoder;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.ZipUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import cyou.wssy001.common.entity.BaseAdminEvent;
import cyou.wssy001.common.entity.BaseEvent;
import cyou.wssy001.common.entity.BasePrivateEvent;
import cyou.wssy001.common.enums.PlatformEnum;
import cyou.wssy001.common.service.CheckUser;
import cyou.wssy001.common.service.DuplicateMessageService;
import cyou.wssy001.common.service.RateLimitService;
import cyou.wssy001.kookadopter.config.KookConfig;
import cyou.wssy001.kookadopter.dto.KookEventDTO;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class KookHttpParamAspect {
    private final KookConfig kookConfig;
    private final CheckUser<KookEventDTO> checkUser;
    private final RateLimitService rateLimitService;
    private final HttpServletRequest httpServletRequest;
    private final HttpServletResponse httpServletResponse;
    private final DuplicateMessageService duplicateMessageService;


    @Pointcut("execution(void cyou.wssy001.kookadopter.controller.KookEventController.handleKookEvent(..))")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object checkKookHttpParam(ProceedingJoinPoint joinPoint) throws Throwable {
        if (!kookConfig.isEnable()) return null;

        String compress = httpServletRequest.getParameter("compress");
        if (StrUtil.isBlank(compress)) compress = "1";
        JSONObject jsonObject;
        try (ServletInputStream inputStream = httpServletRequest.getInputStream()) {
            byte[] bytes = inputStream.readAllBytes();
            log.debug("******KookHttpParamAspect.checkKookHttpParam：传入的数据 BASE64：{}", Base64Encoder.encode(bytes));
            if (compress.equals("1")) {
                jsonObject = unCompress(bytes);
            } else {
                jsonObject = JSON.parseObject(bytes);
            }
        } catch (Exception e) {
            log.error("******KookHttpParamAspect.checkKookHttpParam：无法转换入参，原因：{}", e.getMessage());
            return null;
        }

        if (jsonObject == null || jsonObject.isEmpty()) return null;
        if (jsonObject.containsKey("encrypt"))
            jsonObject = decrypt(jsonObject);

        if (jsonObject == null || jsonObject.isEmpty() || !jsonObject.containsKey("d")) return null;
        KookEventDTO kookEventDTO = verify(jsonObject.getJSONObject("d"));
        if (kookEventDTO == null) return null;

        String channelType = kookEventDTO.getChannelType();
        if (StrUtil.isBlank(channelType)) return null;

        String msgId = kookEventDTO.getMsgId();
        log.info("******KookHttpParamAspect.checkKookHttpParam：{}", msgId);
        if (StrUtil.isNotBlank(msgId) && duplicateMessageService.hasConsumed(msgId, PlatformEnum.KOOK)) {
            log.debug("******KookHttpParamAspect.checkKookHttpParam：消息：{} 已被消费", jsonObject.toJSONString());
            return null;
        }

        String authorId = kookEventDTO.getAuthorId();
        String content = kookEventDTO.getContent();
        String key = ReUtil.getGroup0("^/[一-龥a-zA-z]+", content);

        BaseEvent baseEvent;
        switch (channelType) {
            case "WEBHOOK_CHALLENGE" -> {
                String body = """
                        {
                            "challenge":"%s"
                        }
                        """;
                httpServletResponse.getWriter()
                        .write(String.format(body, kookEventDTO.getChallenge()));
                return null;
            }
            case "PERSON" -> {
                if (StrUtil.isBlank(key)) return null;
                if (!rateLimitService.hasRemain("direct-message/create", PlatformEnum.KOOK)) {
                    log.error("******KookHttpParamAspect.checkKookHttpParam：无法回复 {} 用户：{} 的指令：{}，API限速中", PlatformEnum.KOOK.getDescription(), authorId, key);
                    return null;
                }

                if (checkUser.check(kookEventDTO)) {
                    baseEvent = new BaseAdminEvent()
                            .setEventKey(key)
                            .setPlatform(PlatformEnum.KOOK);
                } else {
                    baseEvent = new BasePrivateEvent()
                            .setEventKey(key)
                            .setPlatform(PlatformEnum.KOOK);
                }
            }
            case "GROUP" -> {
                if (StrUtil.isBlank(key)) return null;
                if (!rateLimitService.hasRemain("message/create", PlatformEnum.KOOK)) {
                    log.error("******KookHttpParamAspect.checkKookHttpParam：无法回复 {} 用户：{} 的指令：{}，API限速中", PlatformEnum.KOOK.getDescription(), authorId, key);
                    return null;
                }

                baseEvent = new BaseEvent()
                        .setEventKey(key)
                        .setPlatform(PlatformEnum.KOOK);
            }
            default -> {
                log.debug("******KookController.handleKookEvent：未知的channelType：{}", channelType);
                return null;
            }
        }

        if (!rateLimitService.hasRemain(authorId, key, PlatformEnum.KOOK)) {
            log.error("******QQHttpParamAspect.checkQQHttpParam：发现 {} 用户：{} 重复请求指令：{}", PlatformEnum.KOOK.getDescription(), authorId, key);
            return null;
        }

        rateLimitService.updateUserLimit(authorId, key, PlatformEnum.KOOK);
        return joinPoint.proceed(new Object[]{baseEvent, kookEventDTO});
    }

    private JSONObject unCompress(byte[] bytes) {
        try {
            String s = ZipUtil.unZlib(bytes, CharsetUtil.UTF_8);
            return JSON.parseObject(s);
        } catch (Exception e) {
            log.error("******KookHttpParamAspect.uncompress：unZlip失败，原因：{}", e.getMessage());
            return null;
        }
    }

    private JSONObject decrypt(JSONObject jsonObject) {
        if (jsonObject == null) jsonObject = new JSONObject();
        String encryptKey = kookConfig.getEncryptKey();
        if (encryptKey == null) {
            log.error("******KookHttpParamAspect.deCrypt：解密失败，请先配置 robot-config.kook.encrypt-key");
            return null;
        }

        String encrypt = jsonObject.getString("encrypt");
        String decrypt = decrypt(encrypt, encryptKey);
        try {
            jsonObject = JSON.parseObject(decrypt);
            return jsonObject;
        } catch (Exception e) {
            log.error("******KookHttpParamAspect.deCrypt：解析Kook消息失败，原因：{}", e.getMessage());
            return null;
        }
    }

    @RegisterReflectionForBinding(KookEventDTO.class)
    private KookEventDTO verify(JSONObject jsonObject) {
        if (jsonObject == null) return null;
        String verifyToken = kookConfig.getVerifyToken();
        if (StrUtil.isBlank(verifyToken)) {
            log.error("******KookHttpParamAspect.verify：请正确配置robot-config.kook.verify-token");
            return null;
        }

        KookEventDTO kookEventDTO = jsonObject.toJavaObject(KookEventDTO.class);
        if (verifyToken.equals(kookEventDTO.getVerifyToken())) return kookEventDTO;
        log.error("******KookHttpParamAspect.verify：verifyToken验证失败");
        return null;
    }

    private String decrypt(String data, String key) {
        String src = new String(Base64.getDecoder().decode(data));
        String iv = src.substring(0, 16);
        byte[] newSecret = Base64.getDecoder().decode(src.substring(16));
        StringBuilder finalKeyBuilder = new StringBuilder(key);
        while (finalKeyBuilder.length() < 32) {
            finalKeyBuilder.append("\0");
        }

        String finalKey = finalKeyBuilder.toString();

        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE,
                    new SecretKeySpec(finalKey.getBytes(), "AES"),
                    new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8)));
            return new String(cipher.doFinal(newSecret));
        } catch (Exception e) {
            log.error("******KookHttpParamAspect.decrypt：解密失败，原因：{}", e.getMessage());
            return null;
        }
    }
}
