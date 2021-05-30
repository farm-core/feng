package com.yun.rabbit.mq.bean;

import com.yun.rabbit.mq.MessageCallBack;
import com.yun.rabbit.mq.manage.RabbitQueueManage;
import com.yun.rabbit.mq.receiver.ReceiveCallback;
import com.yun.rabbit.mq.util.CorrelationUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;

import java.util.List;
import java.util.Map;

/**
 * 消息处理 记录对应队列信息
 *
 * @author wxf
 * @date 2020/3/17
 */
@Data
@Slf4j
public class MessageTemplate {

    private QueueConfig config;

    private RabbitTemplate rabbitTemplate;

    public MessageTemplate(CachingConnectionFactory connectionFactory, QueueConfig config) {
        this(new RabbitTemplate(connectionFactory));
        this.config = config;
    }

    public MessageTemplate(RabbitTemplate rabbitTemplate) {
        this(rabbitTemplate, new ReceiveCallback());
    }

    public MessageTemplate(RabbitTemplate rabbitTemplate, MessageCallBack callBack) {
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitTemplate.setEncoding("UTF-8");
        this.rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        this.rabbitTemplate.setConfirmCallback(callBack);
        this.rabbitTemplate.setReturnCallback(callBack);
    }

    public MessageTemplate(CachingConnectionFactory connectionFactory, MessageCallBack callBack) {
        this(new RabbitTemplate(connectionFactory), callBack);
    }

    public void sendMessage(MessageWrapper wrapper) {
        CorrelationData correlationData = new CorrelationData();
        correlationData.setId(CorrelationUtil.encode(wrapper, config.getName()));
        rabbitTemplate.convertAndSend(config.getExchange(), config.getRoutingKey(), wrapper, correlationData);
    }

    public void sendRetryMessage(Message message) {
        rabbitTemplate.convertAndSend(config.getExchange(), RabbitQueueManage.getRetryRoutingKey(config.getRoutingKey()), message);
    }


    public void sendDeadMessage(Message message) {
        rabbitTemplate.convertAndSend(config.getExchange(), RabbitQueueManage.getDeadRoutingKey(config.getRoutingKey()), message);
    }

    public void sendFailMessage(Message message) {
        long retryCount = getRetryCount(message.getMessageProperties());
        if (retryCount < config.getRetry()) {
            log.error("数据处理失败，当前重试次数 {} error {}", retryCount, new String(message.getBody()));
            sendRetryMessage(message);
        } else {
            log.error("重试全部失败，失败次数 {}  error {}", retryCount, new String(message.getBody()));
            sendDeadMessage(message);
        }
    }

    /**
     * 获取消息被重试的次数
     */
    public static long getRetryCount(MessageProperties messageProperties) {
        Long retryCount = 0L;
        if (null != messageProperties) {
            List<Map<String, ?>> deaths = messageProperties.getXDeathHeader();
            if (deaths != null && deaths.size() > 0) {
                Map<String, Object> death = (Map<String, Object>) deaths.get(0);
                retryCount = (Long) death.get("count");
            }
        }
        return retryCount;
    }
}
