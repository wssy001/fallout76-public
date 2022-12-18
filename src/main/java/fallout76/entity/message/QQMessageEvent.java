package fallout76.entity.message;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@RegisterForReflection
@Accessors(chain = true)
public class QQMessageEvent {

    private long selfId;
    private String postType;
    private long time;
    private String messageType;
    private String message;
    private String subType;
    private String guildId;
    private String channelId;
    private String userId;
    private String messageId;
    private long groupId;
}
