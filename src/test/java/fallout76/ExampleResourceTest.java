package fallout76;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fallout76.config.RobotConfig;
import fallout76.handler.guild.GuildHelpHandler;
import fallout76.restapi.NukaCodeApiService;
import io.quarkus.test.junit.QuarkusTest;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

@Slf4j
@QuarkusTest
public class ExampleResourceTest {

    @Inject
    RobotConfig robotConfig;

    @Inject
    ObjectMapper objectMapper;

    @Inject
    GuildHelpHandler guildHelpHandler;


    @RestClient
    NukaCodeApiService nukaCodeApiService;


    @Test
    public void testHelloEndpoint() {
        log.info("******ExampleResourceTest.testHelloEndpoint：{}",robotConfig);
    }

    @Test
    void test2() {
        guildHelpHandler.execute(null, "");
    }

    @Test
    void test3() throws JsonProcessingException {

        String body = """
                {"operationName":"dashboard","variables":{},"query":"query dashboard {\\n  dashboard {\\n    id\\n    results\\n    __typename\\n  }\\n}\\n"}
                """;
        Response response = nukaCodeApiService.getNukaCode(body);
        JsonNode jsonNode = response.readEntity(JsonNode.class);
        log.info("******ExampleResourceTest.test3：");
    }

    @Test
    void test4() throws JsonProcessingException {

    }

}