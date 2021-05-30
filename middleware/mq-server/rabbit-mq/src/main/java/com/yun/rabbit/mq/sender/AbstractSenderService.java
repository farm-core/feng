package com.yun.rabbit.mq.sender;

import com.yun.rabbit.mq.MessageCallBack;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

/**
 * @program: springboot-rabbit
 * @description: 自定义service 可使用自定义rabbitTemplate
 * @author: wxf
 * @date: 2020-03-02 17:01
 **/
public abstract class AbstractSenderService implements MessageCallBack {
    public RabbitTemplate getRabbitTemplate() {
        return rabbitTemplate;
    }

    private RabbitTemplate rabbitTemplate;

    public void setTemplate(RabbitTemplate template) {
        if (rabbitTemplate == null) {
            rabbitTemplate = template;
        }
    }

    public boolean templateExist() {
        return this.rabbitTemplate != null;
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (!ack) {
            doFail();
        }
    }

    protected abstract void doFail();

    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        doFail();
    }
}
