package com.yun.rabbit.mq.manage;

import com.yun.rabbit.mq.bean.QueueConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * rabbit队列管理
 *
 * @author wxf
 * @date 2020/3/18
 */
@Component
@Slf4j
public class RabbitQueueManage {
    private static final String RETRY_QUEUE_SUFFIX = ".retry";
    private static final String DEAD_QUEUE_SUFFIX = ".dead";
    private static final long DELAY_DUTATION = 10000;

    @Resource
    private RabbitAdmin rabbitAdmin;

    /**
     * 创建Exchange
     *
     * @param exchange 交换机
     */
    private void declareExchange(AbstractExchange exchange) {
        rabbitAdmin.declareExchange(exchange);
    }

    /**
     * 创建一个指定的Queue
     *
     * @param queue
     * @return queueName
     */
    private String declareQueue(Queue queue) {
        return rabbitAdmin.declareQueue(queue);
    }

    /**
     * 绑定一个队列到一个匹配型交换器使用一个routingKey
     *
     * @param queue      队列
     * @param exchange   交换机
     * @param routingKey 路由键
     */
    private void declareBinding(Queue queue, DirectExchange exchange, String routingKey) {
        Binding binding = BindingBuilder.bind(queue).to(exchange).with(routingKey);
        rabbitAdmin.declareBinding(binding);
    }

    private void declareRetryBinding(Queue queue, DirectExchange exchange, String routingKey) {
        Binding binding = BindingBuilder.bind(queue).to(exchange).with(routingKey + RETRY_QUEUE_SUFFIX);
        rabbitAdmin.declareBinding(binding);
    }

    private void declareDeadBinding(Queue queue, DirectExchange exchange, String routingKey) {
        Binding binding = BindingBuilder.bind(queue).to(exchange).with(routingKey + DEAD_QUEUE_SUFFIX);
        rabbitAdmin.declareBinding(binding);
    }


    public void defineQueue(QueueConfig config) {
        defineQueue(config.getName(), config.getExchange(), config.getRoutingKey(), config.getDelay());
    }

    /**
     * 初始化死信队列 重试队列
     *
     * @param queueName    原始队列名称
     * @param exchangeName 交换机名称
     * @param routingKey   路由键
     */
    public void defineQueue(String queueName, String exchangeName, String routingKey, Long delay) {
        DirectExchange exchange = createExchange(exchangeName);
        declareExchange(exchange);
        Queue queue = createQueue(queueName);
        Queue retryQueue = createRetryQueue(queueName, exchangeName, routingKey, delay);
        Queue deadQueue = createDeadQueue(queueName);
        declareQueue(queue);
        declareQueue(retryQueue);
        declareQueue(deadQueue);
        declareBinding(queue, exchange, routingKey);
        declareRetryBinding(retryQueue, exchange, routingKey);
        declareDeadBinding(deadQueue, exchange, routingKey);
    }

    public void defineRetryQueue(String queueName, String exchangeName, String routingKey, Long delay) {
        Queue retryQueue = createRetryQueue(queueName, exchangeName, routingKey, delay);
        DirectExchange exchange = createExchange(exchangeName);
        declareExchange(exchange);
        declareQueue(retryQueue);
        declareRetryBinding(retryQueue, exchange, routingKey);
    }

    public void defineDeadQueue(String queueName, String exchangeName, String routingKey) {
        Queue deadQueue = createDeadQueue(queueName);
        DirectExchange exchange = createExchange(exchangeName);
        declareExchange(exchange);
        declareQueue(deadQueue);
        declareDeadBinding(deadQueue, exchange, routingKey);
    }

    private DirectExchange createExchange(String exchangeName) {
        return new DirectExchange(exchangeName, true, false);
    }

    private Queue createQueue(String queueName) {
        return QueueBuilder.durable(queueName).build();
    }

    private Queue createRetryQueue(String queueName, String exchangeName, String routingKey, Long delay) {
        if (delay == null || delay < 0) {
            delay = DELAY_DUTATION;
        }
        return QueueBuilder.durable(queueName + RETRY_QUEUE_SUFFIX)
                .withArgument("x-message-ttl", delay)
                .withArgument("x-dead-letter-exchange", exchangeName)
                .withArgument("x-dead-letter-routing-key", routingKey)
                .build();
    }

    private Queue createDeadQueue(String queueName) {
        return QueueBuilder.durable(queueName + DEAD_QUEUE_SUFFIX).build();
    }


    public static String getDeadQueueName(String queueName) {
        return queueName + DEAD_QUEUE_SUFFIX;
    }

    public static String getRetryQueueName(String queueName) {
        return queueName + RETRY_QUEUE_SUFFIX;
    }

    public static String getRetryRoutingKey(String routingkey) {
        return routingkey + RETRY_QUEUE_SUFFIX;
    }

    public static String getDeadRoutingKey(String routingkey) {
        return routingkey + DEAD_QUEUE_SUFFIX;
    }

}
