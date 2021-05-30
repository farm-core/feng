package com.yun.rabbit.mq.receiver;

import com.rabbitmq.client.Channel;
import com.yun.rabbit.mq.BaseService;
import com.yun.rabbit.mq.annotation.MsgRetry;
import com.yun.rabbit.mq.bean.MessageTemplate;
import com.yun.rabbit.mq.bean.MessageWrapper;
import com.yun.rabbit.mq.bean.QueueConfig;
import com.yun.rabbit.mq.enums.ActionEnum;
import com.yun.rabbit.mq.util.MessageUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;

/**
 * @program: rabbit-sync
 * @description: 默认消息Listener
 * @author: wxf
 * @date: 2020-03-13 16:08
 **/
@Data
@Slf4j
public class SimpleMessageListener<T extends BaseService> extends AbstractMessageListener {

    public SimpleMessageListener(QueueConfig queueConfig, T service, MessageTemplate messageTemplate) {
        this.queueConfig = queueConfig;
        this.service = service;
        this.messageTemplate = messageTemplate;
    }

    private MessageTemplate messageTemplate;
    private T service;

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        try {
            doAction(message);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }
    }

    @MsgRetry
    private void doAction(Message message) throws Exception {
        MessageWrapper wrapper = MessageUtil.getWrapper(message);
        wrapper.setQueue(this.queueConfig.getName());
        switch (ActionEnum.getOne(wrapper.getAction())) {
            case CREATE:
                service.insert(wrapper);
                return;
            case UPDATE:
                service.update(wrapper);
                return;
            case DELETE:
                service.delete(wrapper);
                return;
            default:
                throw new IllegalStateException("Unexpected value: " + wrapper.getAction());
        }
    }


}
