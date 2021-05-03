package com.rest.api.aop;


import java.time.LocalDateTime;
import java.time.LocalTime;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.Configuration;
import static java.time.temporal.ChronoUnit.SECONDS;;

@Configuration
@Aspect
public class LoggerAspect {

    @Before("@annotation(com.rest.api.aop.MethodLogger)")
    public void beforeMethodStart(JoinPoint point) {
        System.out.println("Method " + point.getSignature().getName() + " Started at " + LocalDateTime.now());

    }

    @After("@annotation(com.rest.api.aop.MethodLogger)")
    public void afterMethodStart(JoinPoint point) {
        System.out.println("Method " + point.getSignature().getName() + " Ended at " + LocalDateTime.now());

    }

    @Around("@annotation(com.rest.api.aop.CalculatePerformance)")
    public void calculate(ProceedingJoinPoint point) {
        LocalTime startTime = LocalTime.now();
        try {
            point.proceed();
        } catch (Throwable e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            LocalTime endTime = LocalTime.now();
            System.out.println("Processing time of Method " + point.getSignature().getName() + " -> "
                    + SECONDS.between(startTime, endTime));
        }

    }

}