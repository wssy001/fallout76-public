package fallout76.controller;

import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import fallout76.config.RobotConfig;
import fallout76.entity.message.QQMessageEvent;
import fallout76.handler.qq.QQBaseGroupHandler;
import fallout76.handler.qq.QQBaseHandler;
import fallout76.handler.qq.QQBasePrivateHandler;
import io.smallrye.common.annotation.RunOnVirtualThread;
import org.jboss.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Path("")
@RunOnVirtualThread
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class QQController {
    private static final Logger LOG = Logger.getLogger(QQController.class);

    @Inject
    ObjectMapper objectMapper;
    @Inject
    RobotConfig robotConfig;

    @Inject
    Instance<QQBaseHandler> qqBaseHandlers;


    private boolean enableQQ;
    private boolean enableQQChannel;

    private final Map<String, QQBaseHandler> qqBaseHandlerMap = new ConcurrentHashMap<>();
    private final Map<String, QQBaseGroupHandler> qqBaseGroupHandlerMap = new ConcurrentHashMap<>();
    private final Map<String, QQBasePrivateHandler> qqBasePrivateHandlerMap = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        Boolean enableQQ = robotConfig.enableQQChannel()
                .orElse(Boolean.FALSE);
        Boolean enableQQChannel = robotConfig.enableQQ()
                .orElse(Boolean.FALSE);

        this.enableQQ = Boolean.TRUE.equals(enableQQ);
        this.enableQQChannel = Boolean.TRUE.equals(enableQQChannel);

        qqBaseHandlers.forEach(qqBaseHandler -> {
            if (qqBaseHandler instanceof QQBaseGroupHandler qqBaseGroupHandler) {
                qqBaseGroupHandler.getKeys()
                        .forEach(key -> qqBaseGroupHandlerMap.put(key, qqBaseGroupHandler));
            } else if (qqBaseHandler instanceof QQBasePrivateHandler qqBasePrivateHandler) {
                qqBasePrivateHandler.getKeys()
                        .forEach(key -> qqBasePrivateHandlerMap.put(key, qqBasePrivateHandler));
            } else {
                qqBaseHandler.getKeys()
                        .forEach(key -> qqBaseHandlerMap.put(key, qqBaseHandler));
            }
        });
    }

    @POST
    @Path("gocqhttp")
    public void webhook(
            QQMessageEvent qqMessageEvent
    ) {
        String postType = qqMessageEvent.getPostType();
        if (!postType.equals("message")) {
            try {
                LOG.info(objectMapper.writeValueAsString(qqMessageEvent));
            } catch (Exception e) {
                LOG.errorf("处理Post Type： %s 异常，原因：%s", postType, e.getCause().toString());
            }
            return;
        }
        String messageType = qqMessageEvent.getMessageType();
        String rawMessage = qqMessageEvent.getRawMessage();
        String key = ReUtil.getGroup0("/[\u4e00-\u9fa5a-z]+", rawMessage);
        if (StrUtil.isBlank(key)) return;
        if (messageType.equals("guild") && enableQQChannel) {
            return;
        } else if (messageType.equals("group") && enableQQ) {
            if (qqBaseGroupHandlerMap.containsKey(key)) {
                QQBaseGroupHandler qqBaseHandler = qqBaseGroupHandlerMap.get(key);
                execute(qqMessageEvent, key, qqBaseHandler);
                return;
            }
            LOG.errorf("指令 %s 未找到", key);
        } else if (messageType.equals("private") && enableQQ) {
            return;
        } else {
            try {
                LOG.info(objectMapper.writeValueAsString(qqMessageEvent));
            } catch (Exception e) {
                LOG.errorf("处理 %s 异常，原因：%s", messageType, e.getCause().toString());
            }
        }
    }

    public void execute(QQMessageEvent qqMessageEvent, String key, QQBaseHandler qqBaseHandler) {
        Thread.ofVirtual()
                .name("处理：" + key + " 指令")
                .start(() -> qqBaseHandler.execute(qqMessageEvent, key));
    }
}
