package com.vanyne.reservation.domain.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

/**
 * @author : Yang Jian
 * @date : 2021/7/4 0004 17:22
 */
@Configuration
public class RedisConfiguration {
    @Autowired
    private RedisConnectionFactory factory;

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer() {
        RedisMessageListenerContainer redisMessageListenerContainer = new RedisMessageListenerContainer();
        redisMessageListenerContainer.setConnectionFactory(factory);
        return redisMessageListenerContainer;
    }
}
