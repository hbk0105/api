package com.rest.api.concurrency;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.UUID;

@Component
@Aspect
public class RedisLockAspect {

    private Logger logger = LoggerFactory.getLogger(RedisLockAspect.class);

    @Resource
    private RedisLockUtil redisLockUtil;
    private String lockKey;
    private String lockValue;

    /** Entry point RedisLock.java , indicating that the @ redislock annotation is used to cut in */
    @Pointcut("@annotation(com.rest.api.concurrency.RedisLock)")
    public void pointcut() {
    }

    @Around(value = "pointcut()")
    public Object around(ProceedingJoinPoint joinPoint)  {
        // Get its @ RedisLock annotation
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        RedisLock redisLock = method.getAnnotation(RedisLock.class);
        // Get the key value equivalent on the annotation
        lockKey = redisLock.key();
        lockValue = redisLock.value();
        if (lockValue.isEmpty()) {
            lockValue = UUID.randomUUID().toString();
        }
        try {
            Boolean isLock = redisLockUtil.lock(lockKey, lockValue, redisLock.expire(), redisLock.timeUnit());
            logger.debug("{} The result of getting the lock is {} ", redisLock.key(), isLock);
            if (!isLock) {
                // Failed to acquire lock
                logger.debug("Failed to acquire lock {}", redisLock.key());
                // You can customize an exception class and its interceptor. See Spring Boot 2.X actual combat -- RESTful API global exception handling
                // https://ylooq.gitee.io/learn-spring-boot-2/#/09-ErrorController?id=spring-boot-2x-%e5%ae%9e%e6%88%98-restful-api-%e5%85%a8%e5%b1%80%e5%bc%82%e5%b8%b8%e5%a4%84%e7%90%86
                // Or @ AfterThrowing: exception throwing enhancement, equivalent to ThrowsAdvice
                throw new RuntimeException("Lock acquisition failed");
            } else {
                try {
                    // The lock is successfully obtained and processed
                    logger.info("The lock is successfully obtained and processed");
                    return joinPoint.proceed();
                } catch (Throwable throwable) {
                    throw new RuntimeException("System abnormality");
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("System abnormality");
        }
    }

    @After(value = "pointcut()")
    public void after() {
        // Release the lock
        if (redisLockUtil.unlock(lockKey, lockValue)) {
            logger.error("redis Distributed lock unlock exception key by {}", lockKey);
        }
    }
}