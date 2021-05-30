package com.yun.kafka.easy.interceptor;

import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Map;

/**
 * @Description: 改拦截器的作用是 在消息发送前将时间戳信息加到消息 value 的最前部
 * @Author: zhenxing
 * @Date: 2020/9/8 17:16
 * @Version: 1.0
 */
public class TimeInterceptor implements ProducerInterceptor {
    /**
     * 改方法封装进KafkaProducer.send方法中，即它运行在用户主线程中。
     * Producer确保在消息被序列化以及计算分区前调用该方法。
     * 用户可以在该方法中对消息做任何操作，但最好保证不要修改消息所属的topic和分区，否则影响目标分区的计算
     */
    @Override
    public ProducerRecord onSend(ProducerRecord record) {
        // 创建一个新的record，把时间戳写入消息体的最前部
        return new ProducerRecord(record.topic(), record.partition(), record.timestamp(), record.key(),
                System.currentTimeMillis() + "," + record.value().toString());
    }

    /**
     * 该方法会在消息从RecordAccumulator成功发送到Kafka broker之后，
     * 或者在发送过程中失败时调用。通常在producer回调逻辑触发之前
     * onAcknowledgement运行在producer的IO线程中，建议不要在改方法中
     * 放入较强的逻辑处理，否则会拖慢producer的消息发送效率
     */
    @Override
    public void onAcknowledgement(RecordMetadata metadata, Exception exception) {

    }

    /**
     * 关闭 interceptor，主要用于执行一些资源清理工作
     */
    @Override
    public void close() {

    }

    /**
     * 获取配置信息和初始化数据时调用
     */
    @Override
    public void configure(Map<String, ?> configs) {

    }
}
