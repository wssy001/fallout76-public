package cyou.wssy001.common.config;

import cyou.wssy001.common.handler.AsyncExceptionHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.support.TaskExecutorAdapter;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@EnableAsync
@Configuration
@RequiredArgsConstructor
public class AsyncConfig implements AsyncConfigurer {
    private final AsyncExceptionHandler asyncExceptionHandler;


    @Bean
    @Primary
    public AsyncTaskExecutor applicationTaskExecutor() {
        ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();
        return new TaskExecutorAdapter(executorService);
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return asyncExceptionHandler;
    }

}
