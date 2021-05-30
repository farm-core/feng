package com.yun.rabbit.mq.config;

import com.yun.rabbit.mq.bean.ExchangeConfig;
import com.yun.rabbit.mq.bean.QueueConfig;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 消息同步配置文件
 *
 * @author wxf
 * @date 2020/3/18
 */
@Data
@Configuration
@ConfigurationProperties("mq-sync")
public class MessageConfiguration {
    String model;

    List<ExchangeConfig> exchanges;

    Map<String, ExchangeConfig> exchangeMap = new HashMap<>();

    Map<String, QueueConfig> queueMap = new HashMap<>();

    @PostConstruct
    public void init() {
        exchanges.forEach(exchangeConfig -> {
            List<QueueConfig> queues = exchangeConfig.getQueues();
            exchangeMap.put(exchangeConfig.getName(), exchangeConfig);
            queues.forEach(queueConfig -> {
                queueConfig.setExchange(exchangeConfig.getName());
                queueMap.put(queueConfig.getName(), queueConfig);
            });
        });
    }

    public QueueConfig getQueue(String queue) {
        return queueMap.get(queue);
    }

    public ExchangeConfig getExchange(String exchange) {
        return exchangeMap.get(exchange);
    }
}
