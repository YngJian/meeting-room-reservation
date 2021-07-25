package com.vanyne.reservation.infrastruction.util;

import com.vanyne.reservation.infrastruction.common.ConstantType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author : Yang Jian
 * @date : 2021/7/20 0020 22:34
 */
@Component
@Slf4j
public class RedisLock {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private DefaultRedisScript<Long> script;

    /**
     * 加锁
     * 使用方式为：
     * lock();
     * try{
     * executeMethod();
     * }finally{
     * unlock();
     * }
     *
     * @param key     键
     * @param value   值
     * @param timeout timeout的时间范围内轮询锁
     * @param expire  设置锁超时时间
     * @return 成功 or 失败
     */
    public boolean lock(String key, String value, long timeout, int expire) {
        LocalDateTime now = LocalDateTime.now();
        timeout *= ConstantType.SECOND_TIME;
        try {
            // 在timeout的时间范围内不断轮询锁
            while (Duration.between(now, LocalDateTime.now()).getSeconds() < timeout) {
                if (stringRedisTemplate.opsForValue().setIfAbsent(key, value, expire, TimeUnit.MINUTES)) {
                    return true;
                }
                log.info("Lock wait...");
                //短暂休眠，避免可能的活锁
                Thread.sleep(3, new Random().nextInt(30));
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }

    public void unlock(String key, String value) {
        try {
            // 释放锁
            String lockValue = stringRedisTemplate.opsForValue().get(key);
            log.info("lockValue ：[{}]", lockValue);
            if (lockValue != null && lockValue.equals(value)) {
                List<String> keys = Collections.singletonList(key);
                Long execute = stringRedisTemplate.execute(script, keys, lockValue);
                log.info("execute execution result [{}]", execute);
            } else {
                log.info("This lock [{}] is not its own, and the release fails.", key);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
