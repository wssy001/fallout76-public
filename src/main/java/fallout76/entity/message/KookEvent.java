package fallout76.entity.message;

import com.fasterxml.jackson.databind.JsonNode;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@RegisterForReflection
@Accessors(chain = true)
public class KookEvent {

    private String channelType;
    private String type;
    private String targetId;
    private String authorId;
    private String msgId;
    private JsonNode jsonNode;
    private String verifyToken;
    private String content;

}
