package com.yun.rabbit.mq.annotation;

import java.lang.annotation.*;

/**
 * 消息队列发送注解
 *
 * @author wxf
 * @date 2020/3/2
 */

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MQClient {
    String exchange() default "";

    String queue() default "";

    String routingKey() default "";

    String config() default "";
}
