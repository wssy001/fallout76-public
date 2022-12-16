package fallout76;

import fallout76.config.RobotConfig;
import fallout76.handler.guild.GuildHelpHandler;
import io.quarkus.arc.All;
import io.quarkus.test.junit.QuarkusTest;
import org.jboss.logging.Logger;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

@QuarkusTest
public class ExampleResourceTest {
    private static final Logger LOG = Logger.getLogger(ExampleResourceTest.class);

    @Inject
    RobotConfig robotConfig;

    @Inject
    GuildHelpHandler guildHelpHandler;


    @Test
    public void testHelloEndpoint() {
        LOG.info(robotConfig);
    }

    @Test
    void test2(){
        guildHelpHandler.execute(null,"");
    }

}