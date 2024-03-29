package cyou.wssy001.baseserviceprovider.schedule;

import cyou.wssy001.common.service.NukaCodeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @Description: 核弹密码定时更新任务
 * @Author: Tyler
 * @Date: 2023/3/15 14:19
 * @Version: 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class NukaCodeScheduledService {
    private final NukaCodeService nukaCodeService;


    // 北京时间 每周日、周一的8时、10时、13时和17时执行
    @Scheduled(cron = "0 0 8,10,13,17 ? * SUN,MON")
    public void refreshNukaCode() {
        log.info("******NukaCodeScheduledService.refreshNukaCode：正在更新NukaCode");
        boolean updated = nukaCodeService.refreshNukaCode(true);

        if (!updated) {
            log.error("******NukaCodeScheduledService.refreshNukaCode：更新NukaCode失败");
            return;
        }
        log.info("******NukaCodeScheduledService.refreshNukaCode：更新NukaCode成功");
    }
}
