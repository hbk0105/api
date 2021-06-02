package com.rest.api.concurrency;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

// 설정 참조 : https://www.fatalerrors.org/a/spring-boot-2.x-implementation-of-a-simple-distributed-lock.html
// Indicates that the method can be annotated
@Target({ElementType.METHOD})
// Annotation retention time, runtime retention
@Retention(RetentionPolicy.RUNTIME)
// automatic succession
@Inherited
@Documented
public @interface RedisLock {
    /** The key value of the lock must be a non empty string */
    @NotNull
    @NotEmpty
    String key();
    /** The value of the lock */
    String value() default "";
    /** Default lock validity 15 by default */
    long expire() default 15;
    /** Time unit of lock validity period, default second */
    TimeUnit timeUnit() default TimeUnit.SECONDS;
}