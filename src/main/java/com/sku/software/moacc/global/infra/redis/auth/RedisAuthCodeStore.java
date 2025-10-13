package com.sku.software.moacc.global.infra.redis.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;

@RequiredArgsConstructor
@Service
public class RedisAuthCodeStore {

    // RedisTemplate<String,String>
    private final StringRedisTemplate template;
    private static final String AUTH_EMAIL_PREFIX = "auth:email:";
    private static final String VERIFIED_SUFFIX = ":verified";
    private static final Duration AUTH_CODE_TTL = Duration.ofMinutes(5);    // 인증코드 TTL: 5분
    private static final Duration VERIFIED_TTL = Duration.ofMinutes(30);    // 인증완료 TTL: 30분

    private String buildKey(String key) {
        return AUTH_EMAIL_PREFIX + key;
    }

    private String buildVerifiedKey(String email) {
        return buildKey(email) + VERIFIED_SUFFIX;
    }

    // key로 value를 가져오는 메소드
    public String getData(String key) {
        ValueOperations<String, String> valueOperations = template.opsForValue();
        return valueOperations.get(buildKey(key));
    }

    public String getVerifiedData(String email) {
        ValueOperations<String, String> valueOperations = template.opsForValue();
        return valueOperations.get(buildVerifiedKey(email));
    }

    // 해당 key에 해당하는 value가 존재하는지 확인하는 메서드
    public boolean existData(String key) {
        return Boolean.TRUE.equals(template.hasKey(buildKey(key)));
    }

    // 이 메소드를 통해 Redis Server에 데이터 삽입
    // key- value 쌍을 저장하는 메소드
    public void setDataExpire(String key, String value, Duration duration) {
        ValueOperations<String, String> valueOperations = template.opsForValue();
        valueOperations.set(buildKey(key), value, duration);
    }

    public void setVerifiedFlag(String email) {
        setDataExpire(email + VERIFIED_SUFFIX, "true", VERIFIED_TTL);
    }

    // key에 해당하는 데이터를 지우는 메소드
    public void deleteData(String key) {
        template.delete(buildKey(key));
    }

    public void deleteVerifiedFlag(String email) {
        template.delete(buildVerifiedKey(email));
    }

    // 인증 코드 저장 (5분)
    public void createRedisData(String email, String code) {
        if (existData(email)) {
            deleteData(email);
        }
        setDataExpire(email, code, AUTH_CODE_TTL);
    }

    // 이메일이 인증되었는지 확인
    public boolean isEmailVerified(String email) {
        String verified = getVerifiedData(email);
        return "true".equals(verified);
    }
}