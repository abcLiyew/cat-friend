package com.esdllm.usercenter.config;

import lombok.Data;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.api.RedissonRxClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Redisson配置类
 */
@Configuration
@ConfigurationProperties(prefix = "spring.data.redis")
@Data
public class RedissonConfig {
    String port;
    String host;
    int redisson_database;
    @Bean
    public RedissonClient redissonClient() {
        // 创建配置
        Config config = new Config();
        String address = String.format("redis://%s:%s", host, port);
        config.useSingleServer()
                .setAddress(address)
                .setDatabase(redisson_database);


//        // 反应式 API
//        RedissonReactiveClient redissonReactive = redisson.reactive();
//
//        // RxJava3 API
//        RedissonRxClient redissonRx = redisson.rxJava();

        // 创建实例
        // 返回同步和异步 API
        return Redisson.create(config);
    }
}
