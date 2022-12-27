package fallout76.service;

import cn.hutool.core.util.StrUtil;
import fallout76.entity.KookLimit;
import io.quarkus.scheduler.Scheduled;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Singleton;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
@Singleton
public class KookAPILimiterService {

    private final ConcurrentHashMap<String, KookLimit> bucketKookLimitMap = new ConcurrentHashMap<>();

    private final BlockingQueue<String> bucketWaitingQueue = new ArrayBlockingQueue<>(30, true);


    public boolean checkPermit(String bucket) {
        if (StrUtil.isBlank(bucket)) return false;
        ReentrantLock lock = new ReentrantLock(true);

        try {
            if (lock.tryLock(3, TimeUnit.SECONDS)) {
                KookLimit limiter = bucketKookLimitMap.get(bucket);
                if (limiter == null) return true;
                return limiter.getRemaining() > 0;
            }
            log.error("******KookAPILimiterService.checkLimit：无法获得锁");
        } catch (Exception e) {
            log.error("******KookAPILimiterService.checkLimit：{}", e.getMessage());
            return false;
        } finally {
            lock.unlock();
        }
        return false;
    }

    public void updateBucketKookLimitMap(String bucket, KookLimit limiter) {
        if (StrUtil.isBlank(bucket) || limiter == null) return;
        ReentrantLock lock = new ReentrantLock(true);

        try {
            if (lock.tryLock(3, TimeUnit.SECONDS)) {
                bucketKookLimitMap.put(bucket, limiter);
                log.info("******KookAPILimiterService.updateBucketKookLimitMap：更新 {} API限速成功", bucket);
                return;
            }

            log.error("******KookAPILimiterService.updateBucketKookLimitMap：无法获得锁");
        } catch (Exception e) {
            log.error("******KookAPILimiterService.updateBucketKookLimitMap：{}", e.getMessage());
        } finally {
            lock.unlock();
        }
    }

    @Scheduled(every = "5s")
    public void addToken() {
        if (bucketWaitingQueue.isEmpty()) return;

        for (String s : bucketWaitingQueue) {
            KookLimit limiter = bucketKookLimitMap.get(s);
            if (limiter == null) {
                bucketWaitingQueue.remove(s);
                continue;
            }
            limiter.setResetWait(limiter.getResetWait() - 5);
            if (limiter.getResetWait() <= 0) {
                bucketWaitingQueue.remove(s);
            }

            limiter.setRemaining(5);
            updateBucketKookLimitMap(s, limiter);

        }
    }

}
