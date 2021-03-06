package com.rest.api.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.*;

import java.time.Duration;

@Configuration
public class CacheConfig {

    // https://deveric.tistory.com/98
    // https://happyer16.tistory.com/entry/Redis-Spring-Boot%EC%97%90-%EC%84%A4%EC%A0%95%ED%95%98%EA%B8%B0-%EB%B0%8F-%EA%B0%9C%EC%9A%94
    // https://j0free.tistory.com/3JSONArray
    // https://www.skyer9.pe.kr/wordpress/?p=1571
    // https://tram-devlog.tistory.com/entry/Spring-EhCache-%EC%A0%81%EC%9A%A9%ED%95%98%EA%B8%B0


    @Autowired
    RedisConnectionFactory redisConnectionFactory;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    RedisConnectionFactory connectionFactory;


    @Bean
    public CacheManager redisCacheManager() {
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                // 시간 설정을 하지 않으면 캐시는 계속 갖고 있음
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer())).entryTtl(Duration.ofMinutes(5L))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.java()));
        RedisCacheManager redisCacheManager = RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(connectionFactory).cacheDefaults(redisCacheConfiguration).build();
        return redisCacheManager;
    }

    // https://stackoverflow.com/questions/59602797/clear-redis-cache-on-spring-boot-application-startup
    @EventListener
    public void onApplicationEvent(ApplicationReadyEvent event) {
        // redis 임시주석
       redisCacheManager().getCacheNames()
                .parallelStream()
                .forEach(n -> redisCacheManager().getCache(n).clear());
    }

}