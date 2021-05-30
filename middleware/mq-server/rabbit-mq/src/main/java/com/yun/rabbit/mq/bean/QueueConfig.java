package com.yun.rabbit.mq.bean;

import com.yun.rabbit.mq.manage.RabbitQueueManage;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: springboot-rabbit - 副本
 * @description:
 * @author: wxf
 * @date: 2020-03-13 17:13
 **/
@Data
@NoArgsConstructor
public class QueueConfig {
    private static final int DEFAULT_RETRY = 2;
    public static final long DEFAULT_TTL = 10000;
    /**
     * 队列名称
     */
    private String name;
    /**
     * 重试次数 0为不重试
     */
    private Integer retry;
    /**
     * 重试延迟时间 毫秒
     */
    private Long delay;
    /**
     * 对应DAO类
     */
    private String clazz;
    /**
     * 交换机名称
     */
    private String exchange;
    /**
     * routingkey
     */
    private String routingKey;

    public QueueConfig(String name, String exchange, String routingKey) {
        this.name = name;
        this.exchange = exchange;
        this.routingKey = routingKey;
    }

    public QueueConfig toDeadQueue() {
        QueueConfig queueConfig = new QueueConfig();
        queueConfig.setName(RabbitQueueManage.getDeadQueueName(this.name));
        queueConfig.setRoutingKey(RabbitQueueManage.getDeadRoutingKey(this.routingKey));
        return queueConfig;
    }

    public Integer getRetry() {
        if (retry == null || retry < 0) {
            return DEFAULT_RETRY;
        }
        return retry;
    }

    public Long getDelay() {
        if (delay == null || delay < 0) {
            return DEFAULT_TTL;
        }
        return delay;
    }
}
