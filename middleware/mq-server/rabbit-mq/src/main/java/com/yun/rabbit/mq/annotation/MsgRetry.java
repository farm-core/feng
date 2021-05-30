package com.yun.rabbit.mq.annotation;

import java.lang.annotation.*;

/**
 * 消息发送失败处理注解
 *
 * @author wxf
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MsgRetry {

}
