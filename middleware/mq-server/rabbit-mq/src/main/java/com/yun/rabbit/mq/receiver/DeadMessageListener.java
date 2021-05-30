package com.yun.rabbit.mq.receiver;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.yun.rabbit.mq.bean.QueueConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @program: springboot-rabbit2
 * @description: 死信消息监听
 * @author: wxf
 * @date: 2020-03-18 14:03
 **/
@Slf4j
public class DeadMessageListener extends AbstractMessageListener {

    public DeadMessageListener(QueueConfig queueConfig) {
        super(queueConfig);
    }

    @Autowired
    private MQClientMonitor mqClientMonitor;

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        // 延迟阻塞
        channel.wait(queueConfig.getDelay() * 10);

        // 获取队列信息
        AMQP.Queue.DeclareOk declareOk = channel.queueDeclarePassive(queueConfig.getName());
        int messageCount = declareOk.getMessageCount();
        // 判断队列中是否有未消费的消息
        if (messageCount > 0) {
            // 重启当前监听的队列
            mqClientMonitor.restartMessageListener(queueConfig.getName());
        } else {
            // 停止当前监听的队列
            mqClientMonitor.stopMessageListener(queueConfig.getName());
        }
    }
}
