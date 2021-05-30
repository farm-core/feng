package com.yun.rabbit.mq.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

/**
 * rabbitmq 配置文件
 *
 * @author wxf
 * @date 2020/3/18
 */
@Configuration
@Slf4j
public class RabbitmqConfig {

    @Value("${spring.rabbitmq.connection.limit:50}")
    private Integer connectionLimit;

    @Bean
    @Lazy
    public RabbitAdmin rabbitAdmin(CachingConnectionFactory cacheConnectionFactory) {
        return new RabbitAdmin(cacheConnectionFactory);
    }
}