package com.yun.rabbit.mq.manage;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @program: springboot-rabbit
 * @description: 连接管理器
 * 官方名称RabbitProperties  可以配置自己的名称 在对应配置文件中加载对应的参数即可
 * @author: wxf
 * @date: 2020-03-09 18:20
 **/
@Component
public class ConnectionManage {
    @Autowired
    private RabbitProperties rabbitProperties;

    private ConcurrentHashMap<String, CachingConnectionFactory> connectionMap = new ConcurrentHashMap<>();


    public synchronized CachingConnectionFactory getConnection(String exchange) {
        CachingConnectionFactory connectionFactory = connectionMap.get(exchange);
        if (connectionFactory == null) {
            connectionFactory = buildFactory(rabbitProperties, exchange);
            connectionMap.put(exchange, connectionFactory);
        }

        return connectionFactory;
    }

    private CachingConnectionFactory buildFactory(RabbitProperties properties, String factoryName) {
        PropertyMapper map = PropertyMapper.get();
        CachingConnectionFactory factory = new CachingConnectionFactory();
        map.from(properties::determineAddresses).to(factory::setAddresses);
        map.from(properties::determineUsername).to(factory::setUsername);
        map.from(properties::determinePassword).to(factory::setPassword);
        map.from(properties::determineVirtualHost).whenNonNull().to(factory::setVirtualHost);
        map.from(properties::isPublisherConfirms).whenNonNull().to(factory::setPublisherConfirms);
        map.from(properties::isPublisherReturns).whenNonNull().to(factory::setPublisherReturns);
        factory.setBeanName(factoryName);
        RabbitProperties.Cache.Channel channel = properties.getCache().getChannel();
        map.from(channel::getSize).whenNonNull().to(factory::setChannelCacheSize);
        map.from(channel::getCheckoutTimeout).whenNonNull().as(Duration::toMillis).to(factory::setChannelCheckoutTimeout);
        factory.setCacheMode(CachingConnectionFactory.CacheMode.CHANNEL);
        return factory;
    }
}
