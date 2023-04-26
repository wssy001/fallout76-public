package cyou.wssy001.common.handler;

import cyou.wssy001.common.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Slf4j
@Component
public class AsyncExceptionHandler implements AsyncUncaughtExceptionHandler {
    @Override
    public void handleUncaughtException(Throwable ex, Method method, Object... params) {
        if (ex instanceof CustomException e) {
            log.error("******AsyncExceptionHandler.handleUncaughtException：异步任务出现异常，方法：{}，信息：{}", method.toGenericString(), e.getMsg());
        }
    }
}
