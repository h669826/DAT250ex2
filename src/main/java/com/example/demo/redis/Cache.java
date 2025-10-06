package com.example.demo.redis;

import org.springframework.stereotype.Service;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

@Service
public class Cache {
    private final StringRedisTemplate redis;

    public Cache(StringRedisTemplate redis) {
        this.redis = redis;
    }

    private String key(UUID pollId){
        return "poll:" + pollId + ":counts";
    }

    public Map<Integer, Long> get(UUID pollId){
        Map<Object, Object> map = redis.opsForHash().entries(key(pollId));
        if(map == null || map.isEmpty()){return null;}
        Map<Integer, Long> map2 = new TreeMap<>();
        map.forEach((k,v)->{map2.put(Integer.parseInt(k.toString()), Long.parseLong(v.toString()));});
        return map2;
    }

    public void put(UUID pollId, Map<Integer, Long> counts, long ttlSeconds){
        String key = key(pollId);
        redis.delete(key);
        counts.forEach((k,v)->{redis.opsForHash().put(key,k.toString(),v.toString());});
        if(ttlSeconds > 0){
            redis.expire(key, Duration.ofSeconds(ttlSeconds));
        }
    }

    public void increment(UUID pollId, int presentationOrder){
        redis.opsForHash().increment(key(pollId), Integer.toString(presentationOrder), 1L);
    }

    public void invalidate(UUID pollId){
        redis.delete(key(pollId));
    }
}
