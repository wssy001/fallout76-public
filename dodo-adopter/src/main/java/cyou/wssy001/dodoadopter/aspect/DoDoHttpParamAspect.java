package cyou.wssy001.dodoadopter.aspect;

import cn.hutool.core.codec.Base64Encoder;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.symmetric.AES;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import cyou.wssy001.common.entity.BaseAdminEvent;
import cyou.wssy001.common.entity.BaseEvent;
import cyou.wssy001.common.entity.BasePrivateEvent;
import cyou.wssy001.common.enums.EventEnum;
import cyou.wssy001.common.enums.PlatformEnum;
import cyou.wssy001.common.service.CheckUser;
import cyou.wssy001.dodoadopter.config.DoDoConfig;
import cyou.wssy001.dodoadopter.dto.DoDoEventDTO;
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

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class DoDoHttpParamAspect {
    private final DoDoConfig dodoConfig;
    private final CheckUser<DoDoEventDTO> checkUser;
    private final HttpServletRequest httpServletRequest;
    private final HttpServletResponse httpServletResponse;


    @Pointcut("execution(void cyou.wssy001.dodoadopter.controller.DoDoHttpEventController.handleDoDoEvent(..))")
    public void pointcut() {
    }

    @Around("pointcut()")
    @RegisterReflectionForBinding(DoDoEventDTO.class)
    public Object checkDoDoHttpParam(ProceedingJoinPoint joinPoint) throws Throwable {
        if (!dodoConfig.isEnable()) return null;
        String secretKey = dodoConfig.getSecretKey();
        if (StrUtil.isBlank(secretKey)) {
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
        String decrypt = decrypt(payload, secretKey);
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

        BaseEvent baseEvent;
        switch (dodoEventDTOData.getEventType()) {
//            消息事件
            case "2001" -> baseEvent = new BaseEvent()
                    .setEventKey(key)
                    .setPlatform(PlatformEnum.DODO)
                    .setEventType(EventEnum.GROUP);
//            私信事件
            case "1001" -> {
                if (checkUser.check(dodoEventDTO)) {
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

        return joinPoint.proceed(new Object[]{baseEvent, dodoEventDTO});
    }

    private String decrypt(String encrypt, String secretKey) {
        byte[] bytes = HexUtil.decodeHex(encrypt);
        byte[] keyBytes = HexUtil.decodeHex(secretKey);
        AES aes = new AES("CBC", "PKCS7Padding", keyBytes, new byte[16]);
        return aes.decryptStr(bytes);
    }

}
