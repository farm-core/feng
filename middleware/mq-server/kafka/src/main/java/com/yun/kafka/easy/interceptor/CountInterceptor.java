package com.yun.kafka.easy.interceptor;

import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Map;

/**
 * @Description: 该拦截器的作用是： 在消息发送后，更新成功发送消息数或失败发送消息数。
 * @Author: zhenxing
 * @Date: 2020/9/8 17:42
 * @Version: 1.0
 */
public class CountInterceptor implements ProducerInterceptor {

    private int errorCounter = 0;
    private int successCounter = 0;

    @Override
    public ProducerRecord onSend(ProducerRecord record) {
        return record;
    }

    @Override
    public void onAcknowledgement(RecordMetadata metadata, Exception exception) {
        if (exception == null) {
            successCounter++;
        } else {
            errorCounter++;
        }

    }

    @Override
    public void close() {
        // 保存结果
        System.out.println("Successful sent: " + successCounter);
        System.out.println("Failed sent: " + errorCounter);
    }

    @Override
    public void configure(Map<String, ?> configs) {

    }
}
