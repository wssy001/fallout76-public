package fallout76.entity;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@RegisterForReflection
@Accessors(chain = true)
public class KookLimit {
    private int limit;

    private int remaining;

    private long resetWait;

    private String bucket;
}
