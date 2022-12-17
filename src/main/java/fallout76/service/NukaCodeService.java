package fallout76.service;

import cn.hutool.core.date.DateTime;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fallout76.entity.NukaCode;
import fallout76.restapi.NukaCodeApiService;
import fallout76.util.PathUtil;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
@Singleton
public class NukaCodeService {
    public static final String BODY = """
            {"operationName":"dashboard","variables":{},"query":"query dashboard {\\n  dashboard {\\n    id\\n    results\\n    __typename\\n  }\\n}\\n"}
            """;

    @Inject
    ObjectMapper objectMapper;
    @RestClient
    NukaCodeApiService nukaCodeApiService;

    private static final Lock lock = new ReentrantLock();
    private final ConcurrentHashMap<String, NukaCode> nukaCodeMap = new ConcurrentHashMap<>();


    @PostConstruct
    public void init() {
        log.info("******NukaCodeService.init：正在读取 nukaCode.json");
        try {
            JsonNode jsonNode = objectMapper.readTree(new File(PathUtil.getJarPath() + "/config/nukaCode.json"));
            NukaCode nukaCode = objectMapper.convertValue(jsonNode, NukaCode.class);
            updateNukaCodeCache(nukaCode);
            log.info("******NukaCodeService.init：读取 nukaCode.json 完毕");
        } catch (IOException e) {
            log.error("******NukaCodeService.init：读取 nukaCode.json 失败，原因：{}", e.getMessage());
        }
    }

    public void updateNukaCodeCache(NukaCode nukaCode) {
        try {
            lock.lock();
            NukaCode newOne = nukaCodeMap.get("new");
            if (newOne != null)
                nukaCodeMap.put("old", newOne);
            nukaCodeMap.put("new", nukaCode);
        } catch (Exception e) {
            log.error("******NukaCodeService.updateNukaCodeCache：更新NukaCodeCache失败，原因：{}", e.getMessage());
        } finally {
            lock.unlock();
        }

    }

    public NukaCode getNukaCode() {
        NukaCode nukaCode = getNukaCodeFromCache();
        if (nukaCode == null) nukaCode = getNukaCodeFromWebsite();
        if (nukaCode != null) updateNukaCodeCache(nukaCode);
        return nukaCode;
    }

    public NukaCode getNukaCodeFromCache() {
        return nukaCodeMap.get("new");
    }

    public NukaCode getNukaCodeFromWebsite() {

        try {
            Response response = nukaCodeApiService.getNukaCode(BODY);
            JsonNode nukaCode = response.readEntity(JsonNode.class);

            if (nukaCode == null || nukaCode.isEmpty()) {
                log.error("******NukaCodeService.getNukaCodeFromWebsite：获取nukaCode失败");
                return null;
            }

            JsonNode nukaCodes = nukaCode.get("data")
                    .get("dashboard")
                    .get("results")
                    .get("nukecodes");

            JsonNode jsonArray = nukaCodes
                    .get("codes");
            ObjectNode objectNode = objectMapper.createObjectNode();
            jsonArray.forEach(object -> objectNode.setAll((ObjectNode) object));

            jsonArray = nukaCodes
                    .get("range");
            DateTime createTime = new DateTime(jsonArray.get(0).asText());
            DateTime expireTime = new DateTime(jsonArray.get(1).asText());
            NukaCode nukaCode1 = objectMapper.convertValue(objectNode, NukaCode.class);
            nukaCode1.setCreateTime(createTime);
            nukaCode1.setExpireTime(expireTime);
            return nukaCode1;
        } catch (Exception e) {
            log.error("******NukaCodeService.getNukaCodeFromWebsite：获取nukaCode失败，原因：{}", e.getMessage());
            return null;
        }
    }
}

