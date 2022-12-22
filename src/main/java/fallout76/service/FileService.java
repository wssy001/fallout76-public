package fallout76.service;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileWriter;
import com.fasterxml.jackson.databind.ObjectMapper;
import fallout76.entity.NukaCode;
import fallout76.util.PathUtil;
import io.quarkus.runtime.StartupEvent;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Singleton;


@Slf4j
@Singleton
public class FileService {

    @Inject
    NukaCodeService nukaCodeService;

    @Inject
    ObjectMapper objectMapper;

    public static final String APPLICATION_YML = """
            quarkus:
              devservices:
                enabled: false
              native:
                additional-build-args: --enable-preview, \\
                  --report-unsupported-elements-at-runtime, \\
                  --trace-object-instantiation=java.lang.Thread, \\
                  -J-XX:+UseStringDeduplication, \\
                  --native-image-info, \\
                  --verbose
              rest-client:
                "fallout76.restapi.GoCQHttpApiService":
                  url: ${robot-config.go-cqhttp-url}
                "fallout76.restapi.KookApiService":
                  url: ${robot-config.kook.open-api-base-url}
                "fallout76.restapi.KookBotMarketApiService":
                  url: ${robot-config.kook.bot-market-online-api-url}
              http:
                port: 35701
              log:
                level: INFO
                file:
                  level: ALL
                  enable: true
                  path: ${user.dir}/log/fallout76-public.log
                  encoding: utf-8
                  rotation:
                    file-suffix: .yyyy-MM-dd
                    max-backup-index: 31
              package:
                type: uber-jar
              scheduler:
                cron-type: spring53
                        
            robot-config:
              #管理员列表，如果是QQ，便是QQ号，如果是Kook则是Kook用户Id，暂不支持QQ频道用户
              admins:
                - 12345
              #Go-cqHttp url 当enable-qq和enable-qq-channel均为false时失效 例： http://127.0.0.1:5700 默认：空
              go-cqhttp-url: http://127.0.0.1:5700
              #是否启用QQ机器人 true | false 默认：false
              enable-qq: true
              #是否启用QQ频道机器人 true | false 默认：false
              enable-qq-channel: true
              #是否启用Kook机器人 true | false 默认：false
              enable-kook: true
                        
              #Kook配置，使用webhook模式，若enable-kook为false则不生效，参数详见https://developer.kookapp.cn/bot/information
              kook:
                #Kook API Base Url 例：https://www.kookapp.cn 默认：空
                open-api-base-url: https://www.kookapp.cn
                #Kook机器人Token 例：sdkasfkashfk 默认：空
                token:
                #Kook机器人Verify Token 例： kjahsdkjaskj 默认：空
                verify-token:
                #Kook机器人Encrypt Key 例：JKHsuiayda ,为空代表不启用 默认：空
                encrypt-key:
                #Kook Bot Market Online接口地址 默认：http://bot.gekj.net
                bot-market-online-api-url:
                #Kook Bot Market UUID
                bot-market-uuid:
                        
            """;

    public static final String PHOTO_JSON = """
            {
              "weeklyNews1": "https://s1.ax1x.com/2022/12/14/zIQRU0.jpg",
              "weeklyNews2": "https://s1.ax1x.com/2022/12/14/zIQW5V.jpg",
              "herbivore": "https://s1.ax1x.com/2022/11/10/zpuzJ1.jpg",
              "carnivore": "https://s1.ax1x.com/2022/11/10/zpuxiR.jpg",
              "magazine": "https://s1.ax1x.com/2022/11/10/zpKpz6.jpg",
              "goldVendorPlans": "https://s1.ax1x.com/2022/11/10/zpKAdH.png",
              "nukaCodeBG": "https://s1.ax1x.com/2022/11/22/z1p5Jf.png",
              "communityCalendar": "https://s1.ax1x.com/2022/11/23/z8nc34.png",
              "goldVendor": "https://s1.ax1x.com/2022/12/08/z2gGuV.png",
              "nukaWorldMap": "https://s1.ax1x.com/2022/12/03/zrG91K.png",
              "nukaWorldGuide1": "https://photo.aws.wssy001.cyou/8c944ffa327a8d8c4657e421629a98bf11144166.jpg",
              "nukaWorldGuide2": "https://s1.ax1x.com/2022/12/19/zLlZHU.jpg",
              "nukaWorldGuide3": "https://s1.ax1x.com/2022/12/19/zLlVBT.png",
              "nukaWorldGuide4": "https://s1.ax1x.com/2022/12/08/z2gTDf.jpg",
              "bobbleheadEffects": "https://s1.ax1x.com/2022/12/14/z5z9xg.png",
              "bobbleheadEffects1": "https://s1.ax1x.com/2022/12/18/zqPFxA.jpg",
              "bobbleheadEffects2": "https://s1.ax1x.com/2022/12/18/zqPi2d.jpg"
            }
            """;



    public void init(@Observes StartupEvent event) {
        boolean exist = FileUtil.isFile(PathUtil.getJarPath() + "/config/nukaCode.json");
        if (!exist) {
            NukaCode nukaCode = nukaCodeService.getNukaCodeFromWebsite();
            if (nukaCode == null) nukaCode = new NukaCode();
            try {
                String json = objectMapper.writeValueAsString(nukaCode);
                generateFile(PathUtil.getJarPath() + "/config", "nukaCode.json", json);
            } catch (Exception e) {
                log.error("******FileService.init：序列化 nukaCode失败，原因：{}", e.getMessage());
            }
        }

        exist = FileUtil.isFile(PathUtil.getJarPath() + "/config/application.yml");
        if (!exist) generateFile(PathUtil.getJarPath() + "/config", "application.yml", APPLICATION_YML);

        exist = FileUtil.isFile(PathUtil.getJarPath() + "/config/photos.json");
        if (!exist) generateFile(PathUtil.getJarPath() + "/config", "photos.json", PHOTO_JSON);
    }

    private void generateFile(String path, String fileName, String fileContent) {
        log.error("******FileService.generateFile：未监测到 {}，正在生成 ......", fileName);
        boolean result = writeFile(path + "/" + fileName, fileContent);
        if (result)
            log.info("******FileService.writeFile：生成 {} 成功", path + "/" + fileName);
    }

    private boolean writeFile(String filePathAndName, String content) {
        FileWriter writer = new FileWriter(filePathAndName);
        try {
            if (content == null) content = "";
            writer.write(content);
            return true;
        } catch (Exception e) {
            log.error("******FileService.writeFile：生成 {} 失败，原因：{}", filePathAndName, e.getMessage());
            return false;
        }
    }
}
