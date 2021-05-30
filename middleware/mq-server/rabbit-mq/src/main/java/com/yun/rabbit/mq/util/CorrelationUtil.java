package com.yun.rabbit.mq.util;

import com.yun.rabbit.mq.bean.BaseWrapper;
import com.yun.rabbit.mq.bean.MessageWrapper;
import org.springframework.amqp.core.Message;

import java.util.UUID;

/**
 * @program: springboot-rabbit
 * @description: CorrelationData 工具
 * @author: wxf
 * @date: 2020-03-05 17:10
 **/
public class CorrelationUtil {
    public static String encode(MessageWrapper wrapper, String queue) {
        return queue + "@" + wrapper.getTag() + "@" + wrapper.getAction() + "@" + UUID.randomUUID().toString();
    }

    public static String decode(String correlationId) {
        String[] split = correlationId.split("@");
        return split[0];
    }

    public static BaseWrapper action(String correlationId) {
        String[] split = correlationId.split("@");
        return new BaseWrapper(split[0], split[1], split[2]);
    }

    public static String getId(Message message) {
        return (String) message.getMessageProperties().getHeaders().get("spring_returned_message_correlation");
    }

}
