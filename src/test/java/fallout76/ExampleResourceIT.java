package fallout76;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import fallout76.entity.NukaCode;
import io.vertx.ext.web.handler.sockjs.impl.StringEscapeUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
public class ExampleResourceIT {
    private final ObjectMapper objectMapper = initObjectMapper();

    ObjectMapper initObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.setAnnotationIntrospector(new JacksonAnnotationIntrospector());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        objectMapper.setDateFormat(dateFormat);
        objectMapper.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        return objectMapper;
    }

    @Test
    void test1() throws Exception {
        String json = """
                {"alpha":"48428717","bravo":"84728291","charlie":"39150256","create_time":"2022-12-11 08:00:00:000","expire_time":"2022-12-18 08:00:00:000"}
                """;

        JsonNode jsonNode = objectMapper.readTree(json);
        NukaCode nukaCode = objectMapper.convertValue(jsonNode, NukaCode.class);
        Date expireTime = nukaCode.getExpireTime();
        long time = expireTime.getTime();
        log.info("******ExampleResourceTest.test4：");
    }

    @Test
    void test2() throws Exception {
        String json = """
                [
                  {
                    "type": "card",
                    "theme": "secondary",
                    "size": "lg",
                    "modules": [
                      {
                        "type": "header",
                        "text": {
                          "type": "plain-text",
                          "content": "指令列表"
                        }
                      },
                      {
                        "type": "divider"
                      },
                      {
                        "type": "section",
                        "text": {
                          "type": "kmarkdown",
                          "content": "`/help\\t/帮助\\t`获取当前环境下所有可用指令\\n"
                        }
                      }
                    ]
                  }
                ]
                """;

        log.info("******ExampleResourceIT.test2：{}", StringEscapeUtils.escapeJava(json));
    }

    @Test
    void test3(){
        Map<String, String> getenv = System.getenv();
        log.info("******ExampleResourceIT.test3：");
    }
}
