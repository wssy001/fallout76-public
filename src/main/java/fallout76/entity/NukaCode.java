package fallout76.entity;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Date;

@Getter
@Setter
@Accessors(chain = true)
@RegisterForReflection
public class NukaCode {
    private String alpha;
    private String bravo;
    private String charlie;
    private Date createTime;
    private Date expireTime;

}
