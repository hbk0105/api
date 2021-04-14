package com.rest.api.config;

import com.rest.api.domain.Token;
import com.rest.api.util.LdapFailAwareRedisObjectSerializer;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.*;

@Configuration
public class RedisConfig {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    // https://hwigyeom.ntils.com/entry/Windows-%EC%97%90-Redis-%EC%84%A4%EC%B9%98%ED%95%98%EA%B8%B0-1
    // https://github.com/microsoftarchive/redis/releases/tag/win-3.2.100
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory("localhost", 6379);
        return lettuceConnectionFactory;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        // https://stackoverflow.com/questions/32751094/spring-boot-with-session-redis-serialization-error-with-bad-active-directory-lda
        // redisTemplate.setHashValueSerializer(new LdapFailAwareRedisObjectSerializer());
        //객체를 json 형태로 깨지지 않고 받기 위한 직렬화 작업
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(Token.class));
        return redisTemplate;
    }

    // redis 세션 임시 주석

    @Bean
    public RedissonClient redissonClient(){
        RedissonClient redisson = Redisson.create();
        return redisson;
    }

/*
    @Bean
    public CacheManager redisCacheManager() {
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.json()));
        redisCacheConfiguration.usePrefix();
        RedisCacheManager redisCacheManager = RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(redisConnectionFactory).cacheDefaults(redisCacheConfiguration).build();
        return redisCacheManager;
    }
*/


}
