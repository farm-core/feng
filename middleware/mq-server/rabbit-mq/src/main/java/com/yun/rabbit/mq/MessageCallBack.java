package com.yun.rabbit.mq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;

/**
 * 消费异常接口
 *
 * @author wxf
 * @date 2020/3/17
 */
public interface MessageCallBack extends RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnCallback {

}
