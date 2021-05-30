package com.yun.rabbit.mq.util;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yun.rabbit.mq.bean.MessageWrapper;
import org.springframework.amqp.core.Message;

import java.io.IOException;

/**
 * @program: springboot-rabbit2
 * @description: 消息实体工具类
 * @author: wxf
 * @date: 2020-03-18 11:06
 **/
public class MessageUtil {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static MessageWrapper getWrapper(Message message) {
        try {
            MessageWrapper messageWrapper = mapper.readValue(message.getBody(), MessageWrapper.class);
            return messageWrapper;
        } catch (IOException e) {
            return null;
        }
    }

    public static <T> T getData(Message message, Class<T> tClass) {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructParametricType(MessageWrapper.class, tClass);
        try {
            MessageWrapper<T> wrapper = mapper.readValue(message.getBody(), javaType);
            return wrapper.getData();
        } catch (IOException e) {
            return null;
        }
    }


}
