package com.wooteco.nolto.auth.infrastructure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class RedisRepository {

    private final StringRedisTemplate template;

    public RedisRepository(StringRedisTemplate template) {
        this.template = template;
    }

    public String leftPop(String key) {
        return template.opsForList().leftPop(key);
    }

    public boolean exist(String key) {
        return Boolean.TRUE.equals(template.hasKey(key));
    }

    public void set(String key, String value, String value2, long expiredIn) {
        log.info("[ value1 : " + value + "] Save Redis Value.");
        template.opsForList().leftPush(key, value);
        log.info("[ value2 : " + value2 + "] Save Redis Value.");
        template.opsForList().leftPush(key, value2);
        template.expire(key, expiredIn, TimeUnit.SECONDS);
    }

    public void delete(String key) {
        template.delete(key);
    }
}

