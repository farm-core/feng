package com.yun.rabbit.mq.bean;

import com.yun.rabbit.mq.enums.ActionEnum;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.Date;

/**
 * @program: springboot-rabbit
 * @description:
 * @author: wxf
 * @date: 2020-03-06 16:04
 **/
@Data
public class BaseWrapper {
    public BaseWrapper(String queue, String tag, String action) {
        this.queue = queue;
        this.tag = tag;
        this.action = Integer.parseInt(action);
    }

    public BaseWrapper() {

    }

    /**
     * 队列名称
     */
    private String queue;
    /**
     * correlationId 数据唯一值标识
     */
    private String tag;
    /**
     * 增删改查
     */
    private Integer action = ActionEnum.CREATE.getValue();

    private Date timestamp = Date.from(ZonedDateTime.now().toInstant());
    ;
}
