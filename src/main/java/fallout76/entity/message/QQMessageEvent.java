package fallout76.entity.message;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class QQMessageEvent {

    private long selfId;
    private String postType;
    private long time;
    private String messageType;
    private String rawMessage;
    private String subType;
    private String guildId;
    private String channelId;
    private String userId;
    private String messageId;
    private long groupId;
}
