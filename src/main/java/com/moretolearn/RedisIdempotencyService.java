package com.moretolearn;

import java.time.Duration;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import tools.jackson.databind.ObjectMapper;

@Service
public class RedisIdempotencyService {

    private static final Duration TTL = Duration.ofMinutes(10);

//    private final StringRedisTemplate redis;
    @Autowired
    private final RedisTemplate<String, String> redis;
    private final ObjectMapper mapper = new ObjectMapper();

    public RedisIdempotencyService(StringRedisTemplate redis) {
        this.redis = redis;
    }

    public <T> T execute(
            String key,
            Supplier<T> action,
            Class<T> type) {

        String redisKey = "idem:" + key;

        // 1️ Already processed → return cached response
        String cached = redis.opsForValue().get(redisKey);
        if (cached != null) {
            return deserialize(cached, type);
        }

        // 2️ Acquire lock (SETNX)
        Boolean locked = redis.opsForValue()
                .setIfAbsent(redisKey + ":lock", "1", TTL);

        if (Boolean.FALSE.equals(locked)) {
            throw new IllegalStateException(
                "Request already in progress"
            );
        }

        try {
            // 3️ Execute business logic
            T result = action.get();

            // 4️ Cache response
            redis.opsForValue().set(
                redisKey,
                serialize(result),
                TTL
            );

            return result;

        } finally {
            // 5️ Release lock
            redis.delete(redisKey + ":lock");
        }
    }

    private <T> T deserialize(String json, Class<T> type) {
        try {
            return mapper.readValue(json, type);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String serialize(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

