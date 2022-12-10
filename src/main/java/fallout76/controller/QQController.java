package fallout76.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fallout76.config.RobotConfig;
import fallout76.entity.message.QQMessageEvent;
import io.smallrye.common.annotation.RunOnVirtualThread;
import org.jboss.logging.Logger;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

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


    private boolean enableQQ;
    private boolean enableQQChannel;

    @PostConstruct
    public void init() {
        Boolean enableQQ = robotConfig.enableQQChannel()
                .orElse(Boolean.FALSE);
        Boolean enableQQChannel = robotConfig.enableQQ()
                .orElse(Boolean.FALSE);

        this.enableQQ = Boolean.TRUE.equals(enableQQ);
        this.enableQQChannel = Boolean.TRUE.equals(enableQQChannel);
    }

    @POST
    @Path("gocqhttp")
    public void webhook(
            QQMessageEvent qqMessageEvent
    ) {

        String messageType = qqMessageEvent.getMessageType();
        if (messageType.equals("guild") && enableQQChannel) {

        } else if (messageType.equals("group") && enableQQ) {

        } else if (messageType.equals("private") && enableQQ) {

        } else {
            try {
                LOG.info(objectMapper.writeValueAsString(qqMessageEvent));
            } catch (Exception e) {
                LOG.errorf("处理 %s 异常，原因：%s", messageType, e.getCause().toString());
            }
        }
    }
}
