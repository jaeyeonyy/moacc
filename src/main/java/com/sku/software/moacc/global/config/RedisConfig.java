package com.sku.software.moacc.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedisConfig {
    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    // application.yml에 있는 port와 host를 불러와서 Redis와 연결해주는 역할
    // Redis 서버 연결을 만드는 객체
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(host, port);
    }

    // RedisConnection을 통해 넘어온 byte값을 객체 직렬화(Serialization)
    // 위에서 만들어진 연결을 이용하여 실제로 데이터 조작을 수행하는 객체
    @Bean
    public RedisTemplate<?, ?> redisTemplate() {
        RedisTemplate<byte[], byte[]> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory((redisConnectionFactory()));
        return redisTemplate;
    }
}