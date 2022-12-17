package fallout76;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import fallout76.entity.NukaCode;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

@Slf4j
public class ExampleResourceIT {
    private final ObjectMapper objectMapper = initObjectMapper();

    ObjectMapper initObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.setAnnotationIntrospector(new JacksonAnnotationIntrospector());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        objectMapper.setDateFormat(dateFormat);
        objectMapper.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.LOWER_CAMEL_CASE);
        return objectMapper;
    }

    @Test
    void test1() throws Exception {
        String json = """
                {"alpha":"48428717","bravo":"84728291","charlie":"39150256","create_time":"2022-12-11 08:00:00","expire_time":"2022-12-18 08:00:00"}
                """;

        JsonNode jsonNode = objectMapper.readTree(json);
        NukaCode nukaCode = objectMapper.convertValue(jsonNode, NukaCode.class);
        log.info("******ExampleResourceTest.test4：");
    }
}
