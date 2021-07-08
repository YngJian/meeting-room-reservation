package com.vanyne.reservation.domain.listener;

import com.vanyne.reservation.infrastruction.common.ConstantType;
import com.vanyne.reservation.infrastruction.common.StatusType;
import com.vanyne.reservation.infrastruction.repository.UserInfoRepository;
import com.vanyne.reservation.infrastruction.repository.db.entity.UserInfoEntity;
import com.vanyne.reservation.infrastruction.util.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * redis配置notify-keyspace-events Ex 设置过期监听
 * 自动解锁，注：如果服务器停止，过期键监听无效
 * 单机redis，使用setnx+lua脚本为分布式锁
 * 集群redis，使用redLock为分布式锁https://github.com/YngJian/redis-redlock
 *
 * @author : Yang Jian
 * @date : 2021/7/4 0004 17:18
 */
@Component
@Slf4j
public class AutoUnlockUserListener extends KeyExpirationEventMessageListener {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private DefaultRedisScript<Long> script;

    public AutoUnlockUserListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        // 获取过期的key
        String expireKey = message.toString();
        if (!expireKey.contains(ConstantType.PWD_AUTO_UNLOCK_KEY)) {
            return;
        }
        log.info("The expiration key is: [{}]", expireKey);

        String userId = expireKey.replaceAll(ConstantType.PWD_AUTO_UNLOCK_KEY, "");
        log.info("Automatically unlock. user id: [{}]", userId);

        // 多服务一台操作,抢锁
        String key = ConstantType.AUTO_UNLOCK_REDIS_KEY + userId;
        String value = CommonUtils.getUUID();
        Boolean setIfAbsent = stringRedisTemplate.opsForValue().setIfAbsent(key, value, ConstantType.
                AUTO_UNLOCK_REDIS_EXPIRE_HOURS, TimeUnit.HOURS);

        try {
            if (setIfAbsent != null && !setIfAbsent) {
                log.info("Failed to acquire lock, key: [{}] ", key);
                return;
            }
            UserInfoEntity userInfoEntity = UserInfoEntity.builder()
                    .status(StatusType.NORMAL)
                    .build();
            userInfoRepository.updateByUserId(userId, userInfoEntity);
        } finally {
            // 释放锁
            String lockValue = stringRedisTemplate.opsForValue().get(key);
            log.info("lockValue ：[{}]", lockValue);
            if (lockValue != null && lockValue.equals(value)) {
                List<String> keys = Collections.singletonList(key);
                Long execute = stringRedisTemplate.execute(script, keys, lockValue);
                log.info("execute execution result [{}], key [{}] unlock successfully end processing business", execute, key);
            } else {
                log.info("This lock [{}] is not its own, and the release fails.", key);
            }
        }
    }
}
