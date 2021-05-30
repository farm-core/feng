package com.yun.rabbit.mq.bean;

import lombok.Data;

import java.util.List;

/**
 * 路由配置
 *
 * @author wxf
 * @date 2020/3/18
 */
@Data
public class ExchangeConfig {
    public static final int DEFAULT_LISTENER_NUM = 1;
    private String name;
    private Integer listener;
    private List<QueueConfig> queues;

    public Integer getListener() {
        return listener == null ? DEFAULT_LISTENER_NUM : listener;
    }
}
