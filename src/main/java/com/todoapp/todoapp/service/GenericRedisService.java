package com.todoapp.todoapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GenericRedisService {
    private final RedisTemplate<String, Object> redisTemplate;

    public <T> void save(String hashName, String id, T object) {
        redisTemplate.opsForHash().put(hashName, id, object);
    }

    public <T> T get(String hashName, String id, Class<T> clazz) {
        Object obj = redisTemplate.opsForHash().get(hashName, id);
        if(obj != null) return clazz.cast(obj);
        return null;
    }

    // Xóa object trong Redis hash
    public void delete(String hashName, String id) {
        redisTemplate.opsForHash().delete(hashName, id);
    }

    // Lấy tất cả object trong Redis hash
    public Map<Object, Object> getAll(String hashName) {
        return redisTemplate.opsForHash().entries(hashName);
    }

    public void deleteBatch(String key, List<String> ids) {
        redisTemplate.opsForHash().delete(key, ids.toArray());
    }
}
