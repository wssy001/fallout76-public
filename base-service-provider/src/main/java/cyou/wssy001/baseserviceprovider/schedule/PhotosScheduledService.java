package cyou.wssy001.baseserviceprovider.schedule;

import cyou.wssy001.common.service.PhotoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @Description: 图片信息定时更新任务
 * @Author: Tyler
 * @Date: 2023/3/15 14:19
 * @Version: 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PhotosScheduledService {
    private final PhotoService photoService;


    // 每天早上8时执行一次
    @Scheduled(cron = "0 0 8 * * ?")
    public void refreshPhotos() {
        log.info("******PhotosScheduledService.refreshPhotos：正在更新图片信息");
        boolean updated = photoService.refreshPhotos(true);

        if (!updated) {
            log.error("******PhotosScheduledService.refreshPhotos：更新图片信息失败");
            return;
        }
        log.info("******PhotosScheduledService.refreshPhotos：更新图片信息成功");
    }
}
