package com.example.sell.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@Slf4j
public class RedisLock {

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 获取分布式锁
     * @param key 待锁定的key，如果是商品可以商品的ID
     * @param value 系统时间+过期时间.
     * @return 如果获得锁则返回true，否则返回false.
     */
    public boolean lock(String key,String value){
        if (redisTemplate.opsForValue().setIfAbsent(key, value)) {
            return true;
        }
        String currentValue = redisTemplate.opsForValue().get(key);
        //已过期
        if(!StringUtils.isEmpty(currentValue)
                && Long.parseLong(currentValue) < System.currentTimeMillis()){
            String oldValue = redisTemplate.opsForValue().getAndSet(key, value);
            if(!StringUtils.isEmpty(oldValue)
                    && currentValue.equals(oldValue)){
                return true;
            }
        }
        return false;
    }

    /**
     * 释放锁
     * @param key 待锁定的key，如果是商品可以商品的ID
     * @param value 系统时间+过期时间.
     */
    public void unlock(String key,String value){
        try {
            String currentValue = redisTemplate.opsForValue().get(key);
            if (!StringUtils.isEmpty(currentValue)
                    && currentValue.equals(value)) {
                redisTemplate.opsForValue().getOperations().delete(key);
            }
        } catch (Exception e) {
            log.error("【redis分布式锁】释放锁异常，{}",e );
        }
    }
}
