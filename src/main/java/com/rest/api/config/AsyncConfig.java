package com.rest.api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

// https://heowc.dev/2018/02/10/spring-boot-async/ , https://springboot.tistory.com/38 , https://gofnrk.tistory.com/34
// https://akageun.github.io/2019/06/04/spring-async-1.html
// https://cofs.tistory.com/319 , https://brunch.co.kr/@springboot/401
@Configuration
@EnableAsync
public class AsyncConfig extends AsyncConfigurerSupport {

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("heowc-async-");
        executor.initialize();
        return executor;
    }
}