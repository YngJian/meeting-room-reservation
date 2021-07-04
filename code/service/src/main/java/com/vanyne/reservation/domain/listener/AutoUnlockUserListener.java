package com.vanyne.reservation.domain.listener;

import com.vanyne.reservation.infrastruction.common.ConstantType;
import com.vanyne.reservation.infrastruction.common.UserStatusType;
import com.vanyne.reservation.infrastruction.repository.UserInfoRepository;
import com.vanyne.reservation.infrastruction.repository.db.entity.UserInfoEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 自动解锁，注：如果服务器停止，过期键监听无效
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

    public AutoUnlockUserListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        //获取过期的key
        String expireKey = message.toString();
        if (!expireKey.contains(ConstantType.PWD_AUTO_UNLOCK_KEY)) {
            return;
        }
        log.info("The expiration key is: [{}]", expireKey);

        String userId = expireKey.replaceAll(ConstantType.PWD_AUTO_UNLOCK_KEY, "");
        log.info("Automatically unlock. user id: [{}]", userId);

        // 多服务一台操作
        String key = ConstantType.AUTO_UNLOCK_REDIS_KEY + userId;
        Long increment = stringRedisTemplate.opsForValue().increment(key);
        stringRedisTemplate.expire(key, ConstantType.AUTO_UNLOCK_REDIS_EXPIRE_HOURS, TimeUnit.HOURS);
        if (increment == null || increment != 1) {
            return;
        }

        UserInfoEntity userInfoEntity = UserInfoEntity.builder()
                .status(UserStatusType.NORMAL)
                .build();
        userInfoRepository.updateByUserId(userId, userInfoEntity);
    }
}
