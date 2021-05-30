package com.yun.rabbit.mq.bean;

import lombok.Data;

/**
 * @program: springboot-rabbit
 * @description: 消息包装
 * @author: wxf
 * @date: 2020-02-24 11:13
 **/
@Data
public class MessageWrapper<T> extends BaseWrapper {
    /**
     * 数据实体
     */
    private T data;

    public MessageWrapper(String queue, String tag, String action, T data) {
        super(queue, tag, action);
        this.data = data;
    }

    public MessageWrapper() {

    }
}
