package com.yun.rabbit.mq.receiver;

import com.yun.rabbit.mq.bean.QueueConfig;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;

/**
 * @program: springboot-rabbit2
 * @description:
 * @author: wxf
 * @date: 2020-03-18 14:18
 **/
@Data
@NoArgsConstructor
public abstract class AbstractMessageListener implements ChannelAwareMessageListener {
    public QueueConfig queueConfig;

    public AbstractMessageListener(QueueConfig queueConfig) {
        this.queueConfig = queueConfig;
    }
}
