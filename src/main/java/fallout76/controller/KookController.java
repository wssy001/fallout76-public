package fallout76.controller;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.ZipUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fallout76.config.RobotConfig;
import fallout76.entity.message.KookEvent;
import fallout76.handler.KookBaseHandler;
import fallout76.handler.kook.KookGuildHandler;
import fallout76.handler.kook.KookPrivateHandler;
import fallout76.util.CryptUtil;
import io.smallrye.common.annotation.RunOnVirtualThread;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Path("kook")
@RunOnVirtualThread
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class KookController {

    @Inject
    ObjectMapper objectMapper;
    @Inject
    RobotConfig robotConfig;
    @Inject
    Instance<KookBaseHandler> kookBaseHandlers;

    private final Map<String, KookBaseHandler> kookBaseHandlerMap = new ConcurrentHashMap<>();
    private final Map<String, KookGuildHandler> kookGuildHandlerMap = new ConcurrentHashMap<>();
    private final Map<String, KookPrivateHandler> kookPrivateHandlerMap = new ConcurrentHashMap<>();

    private boolean enableKook;
    private String encryptKey;
    private String verifyToken;


    @PostConstruct
    public void init() {
        Boolean kook = robotConfig.enableKook()
                .orElse(Boolean.FALSE);

        this.enableKook = Boolean.TRUE.equals(kook);

        encryptKey = robotConfig.kook()
                .encryptKey()
                .orElse(null);

        verifyToken = robotConfig.kook()
                .verifyToken()
                .orElse(null);

        kookBaseHandlers.forEach(kookBaseHandler -> {
            switch (kookBaseHandler) {
                case KookGuildHandler kookGuildHandler -> kookBaseHandler.getKeys()
                        .forEach(key -> kookGuildHandlerMap.put(key, kookGuildHandler));
                case KookPrivateHandler kookPrivateHandler -> kookPrivateHandler.getKeys()
                        .forEach(key -> kookPrivateHandlerMap.put(key, kookPrivateHandler));
                case null, default -> kookBaseHandler.getKeys()
                        .forEach(key -> kookBaseHandlerMap.put(key, kookBaseHandler));
            }
        });
    }

    @POST
    @Path("webhook")
    public JsonNode webhook(
            @QueryParam("compress") Integer compress,
            byte[] bytes
    ) {
        if (!enableKook) return null;

        JsonNode jsonNode;
        if (compress == null || compress.equals(1)) {
            jsonNode = uncompress(bytes);
        } else {
            try {
                jsonNode = objectMapper.readTree(bytes);
            } catch (Exception e) {
                log.error("******KookController.webhook：转换成JsonNode失败，原因：{}", e.getMessage());
                return null;
            }
        }

        if (jsonNode.has("encrypt"))
            jsonNode = deCrypt(jsonNode);

        jsonNode = jsonNode.get("d");
        KookEvent kookEvent = verify(jsonNode);
        if (kookEvent == null) return null;

        String channelType = kookEvent.getChannelType();
        if (channelType.equals("WEBHOOK_CHALLENGE")) return jsonNode;

        String content = kookEvent.getContent();
        String key = ReUtil.getGroup0("^/[\u4e00-\u9fa5a-z]+", content);
        if (StrUtil.isBlank(key)) return null;
        if (channelType.equals("PERSON")) {
            KookPrivateHandler kookPrivateHandler = kookPrivateHandlerMap.get(key);
            if (kookPrivateHandler != null) {

                return null;
            }
            log.error("******QQController.webhook：指令 Kook PERSON： {} 未找到", key);
        } else if (channelType.equals("GROUP")) {
            KookGuildHandler kookGuildHandler = kookGuildHandlerMap.get(key);
            if (kookGuildHandler != null) {
                execute(kookEvent, key, kookGuildHandler);
                return null;
            }
            log.error("******QQController.webhook：指令 Kook GROUP： {} 未找到", key);
        } else {
            log.info("******KookController.webhook：\n{}", jsonNode.toPrettyString());
        }

        return null;
    }

    private JsonNode uncompress(byte[] bytes) {
        try {
            String s = ZipUtil.unZlib(bytes, CharsetUtil.UTF_8);
            return objectMapper.readTree(s);
        } catch (IOException e) {
            log.error("******KookController.uncompress：unzlip失败，原因：{}", e.getMessage());
            return objectMapper.createObjectNode();
        }
    }

    private JsonNode deCrypt(JsonNode jsonNode) {
        if (jsonNode == null || jsonNode.isEmpty()) return objectMapper.createObjectNode();
        if (encryptKey == null) {
            log.error("******KookController.deCrypt：解密失败，请先配置 robot-config.kook.encrypt-key");
            return objectMapper.createObjectNode();
        }

        String encrypt = jsonNode.get("encrypt")
                .asText();
        String decrypt = CryptUtil.decrypt(encrypt, encryptKey);

        try {
            return objectMapper.readTree(decrypt);
        } catch (JsonProcessingException e) {
            log.error("******KookController.deCrypt：转换成JsonNode失败，原因：{}", e.getMessage());
            return objectMapper.createObjectNode();
        }
    }

    private KookEvent verify(JsonNode jsonNode) {
        if (jsonNode == null || jsonNode.isEmpty()) return null;
        if (StrUtil.isBlank(verifyToken)) {
            log.error("******KookController.verify：请正确配置robot-config.kook.verify-token");
            return null;
        }

        KookEvent kookEvent = objectMapper.convertValue(jsonNode, KookEvent.class);
        String verifyToken = kookEvent.getVerifyToken();
        if (this.verifyToken.equals(verifyToken)) return kookEvent;
        log.error("******KookController.verify：verifyToken验证失败");
        return null;
    }

    public void execute(KookEvent kookEvent, String key, KookBaseHandler kookBaseHandler) {
        kookBaseHandler.execute(kookEvent, key);
    }
}
