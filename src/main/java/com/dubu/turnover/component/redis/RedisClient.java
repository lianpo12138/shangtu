package com.dubu.turnover.component.redis;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.dubu.turnover.configure.Configurer;
import com.dubu.turnover.utils.JsonUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class RedisClient {

    @Autowired
    private RedisTemplate  redisTemplate;
    @Autowired
    private StringRedisTemplate template;

    public <T> void put(String key, T value) {
        if (StringUtils.isBlank(key)) {
            throw new NullPointerException("redis key can't be empty...");
        }
        redisTemplate.opsForValue().set(key, value);
    }

    public <T> void put(String key, T value, Long timeout) {
        if (StringUtils.isBlank(key)) {
            throw new NullPointerException("redis key can't be empty...");
        }
        redisTemplate.opsForValue().set(key, value,timeout,TimeUnit.SECONDS);
    }

    public <T> T get(String key) {
        if (StringUtils.isBlank(key)) {
            throw new NullPointerException("redis key can't be empty...");
        }
        return (T) redisTemplate.opsForValue().get(key);
    }

    public Boolean hasKey(String key) {
        if (StringUtils.isBlank(key)) {
            throw new NullPointerException("redis key can't be empty...");
        }
        return redisTemplate.hasKey(key);
    }

    public void delete(String key) {
        if (StringUtils.isBlank(key)) {
            throw new NullPointerException("redis key can't be empty...");
        }
        redisTemplate.delete(key);
    }

    public void incrBy(String key, Long increment) {
        if (StringUtils.isBlank(key)) {
            throw new NullPointerException("redis key can't be empty...");
        }
        redisTemplate.opsForValue().increment(key, increment);
    }

    public <T> void rListPush(String key, T value) {
        if (StringUtils.isBlank(key)) {
            throw new NullPointerException("redis key can't be empty...");
        }
        redisTemplate.opsForList().rightPush(key, value);
    }


    public List<Object> getAllList(String key) {
        if (StringUtils.isBlank(key)) {
            throw new NullPointerException("redis key can't be empty...");
        }
        return redisTemplate.opsForList().range(key, 0, -1);
    }

    public <T> void lListPush(String key, T value) {
        if (StringUtils.isBlank(key)) {
            throw new NullPointerException("redis key can't be empty...");
        }
        redisTemplate.opsForList().leftPush(key, value);
    }

    public Long listSize(String key) {
        if (StringUtils.isBlank(key)) {
            throw new NullPointerException("redis key can't be empty...");
        }
        return redisTemplate.opsForList().size(key);
    }

    public <T> T rListPop(String key) {
        if (StringUtils.isBlank(key)) {
            throw new NullPointerException("redis key can't be empty...");
        }
        return (T) redisTemplate.opsForList().rightPop(key);
    }

    public void watch(String... keys) {
        List<String> keyList = new ArrayList<>(Arrays.asList(keys));
        redisTemplate.watch(keyList);
    }

    public void watch(String key) {
        if (StringUtils.isBlank(key)) {
            throw new NullPointerException("redis key can't be empty...");
        }
        redisTemplate.watch(key);
    }

    public <T> void addZet(String key, T value, double scores) {
        if (StringUtils.isBlank(key)) {
            throw new NullPointerException("redis key can't be empty...");
        }
        redisTemplate.opsForZSet().add(key, value, scores);
    }

    /**
     * 操作redis获取全局锁
     *
     * @param key            锁的名称
     * @param timeout        获取的超时时间
     * @param tryInterval    多少ms尝试一次
     * @param lockExpireTime 获取成功后锁的过期时间
     * @return true 获取成功，false获取失败
     */
    public boolean getLock(String key, long timeout, long tryInterval, long lockExpireTime) {
        try {
            if (!org.springframework.util.StringUtils.isEmpty(key)) {
                long startTime = System.currentTimeMillis();
                while (true) {
                    String lock = "lock";
                    if (template.opsForValue().setIfAbsent(key, lock)) {
                        template.opsForValue().set(key, lock, lockExpireTime, TimeUnit.MILLISECONDS);
                        return true;
                    }
                    if (System.currentTimeMillis() - startTime > timeout) {
                        return false;
                    }
                    Thread.sleep(tryInterval);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public void releaseLock(String key) {
        if (!org.springframework.util.StringUtils.isEmpty(key)) {
            template.delete(key);
        }
    }
    public <K, V> RedisTemplate<K, V> template() {
        return redisTemplate;
    }

    /**
     * redis 原子性增长
     *
     * @param key
     */
    public Integer increment(String key) {
        if (StringUtils.isBlank(key)) {
            throw new NullPointerException("redis key can't be empty...");
        }
        ValueOperations<String, Integer> operations = redisTemplate.opsForValue();
        Long value = operations.increment(key, 1);
        return value.intValue();
    }

    /**
     * 设置过期时间
     *
     * @param key
     * @param timeout
     * @param unit
     */
    public void expire(String key, long timeout, TimeUnit unit) {
        redisTemplate.expire(key, timeout, unit);
    }

    public Boolean putHash(String key) {
        BoundHashOperations<String, String, String> hashOperations = redisTemplate.boundHashOps(Configurer.HASH_KEY);
        hashOperations.expire(1, TimeUnit.DAYS);
        return hashOperations.putIfAbsent(key, "1");
    }
    
    public static String userAuctionKey(Integer userId, String application) {
        return String.format("%s:BID:USER:%d:AUCTIONS", application, userId);
    }
    
    public Boolean hashKey(String key,String value) {
        if (StringUtils.isBlank(key)) {
            throw new NullPointerException("redis key can't be empty...");
        }
        return redisTemplate.opsForHash().hasKey(key, value);
    }
    public <T> T getKey(String key) {
        return (T) redisTemplate.opsForValue().get(key);
    }
    public static String userKey(Integer userId, String application) {
        return String.format("%s:BID:USER:%d", application, userId);
    }
    
    public <T> T getHashKey(String key,String hashKey) {
        if (StringUtils.isBlank(key)) {
            throw new NullPointerException("redis key can't be empty...");
        }
        return (T) redisTemplate.opsForHash().get(key,hashKey);
    }

}