package com.rest.api.config;

import com.rest.api.interceptor.AuthInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// https://aljjabaegi.tistory.com/531
 @Configuration
 public class WebMvcConfig implements WebMvcConfigurer{

     @Autowired
     AuthInterceptor authInterceptor;

     @Override
     public void addInterceptors(InterceptorRegistry registry) {
         registry.addInterceptor(new AuthInterceptor()) // 메소드를 오버라이드 하여 이전에 작성한 interceptor를 추가해줍니다.
         //registry.addInterceptor(authInterceptor) // 메소드를 오버라이드 하여 이전에 작성한 interceptor를 추가해줍니다.
                 //  말그대로 인터셉터가 동작할 url 패턴
                 .addPathPatterns("/users/**")
                 .excludePathPatterns("api/login"); // 인터셉터가 동작하지 않을 url 패턴입니다.
     }

 }
