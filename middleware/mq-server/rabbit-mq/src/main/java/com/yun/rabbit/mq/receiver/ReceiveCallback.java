package com.yun.rabbit.mq.receiver;

import com.yun.rabbit.mq.MessageCallBack;
import com.yun.rabbit.mq.util.CorrelationUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;

import java.util.Arrays;

/**
 * @program: springboot-rabbit2
 * @description: 接收端callback
 * @author: wxf
 * @date: 2020-03-18 11:57
 **/
@Slf4j
public class ReceiveCallback implements MessageCallBack {

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        // 消费失败进行日志记录方便后期的重新消费
        if (!ack) {
//            callback(correlationData.getId());
        }
    }

    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        log.info(exchange + " : " + Arrays.toString(message.getBody()));
        String correlationId = CorrelationUtil.getId(message);
//        callback(correlationId);
    }

    /**
     * callback日志记录-ES
     *
     * @param correlationId
     */
    /*private void callback(String correlationId) {
        String queue = CorrelationUtil.decode(correlationId);
        // 获取接收端日志记录
        DefaultStorageClient client = StorageClientManage.get(queue);
        ThreadPoolManager.getsInstance().execute(() -> {
            client.storage(correlationId);
        });
    }*/
}
