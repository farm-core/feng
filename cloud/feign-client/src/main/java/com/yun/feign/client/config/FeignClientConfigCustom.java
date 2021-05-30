package com.yun.feign.client.config;

import feign.Logger;
import feign.Request;
import feign.Retryer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wxf
 * @Description 自定义feign client 配置
 * @date 2020/8/5
 */
@Configuration
public class FeignClientConfigCustom {

    /**
     * 配置 Feign 日志级别
     * <p>
     * NONE：没有日志
     * BASIC：基本日志
     * HEADERS：header
     * FULL：全部
     * <p>
     * 配置为打印全部日志，可以更方便的查看 Feign 的调用信息
     *
     * @return Feign 日志级别
     */
    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.BASIC;
    }

    /**
     * 自定义重试Retryer
     * maxAttempts feignClient重试次数
     *
     * @author zhanggk
     * 2019/8/5 16:03
     */
    @Bean
    public Retryer retryer() {
        return new Retryer.Default(100, 1000, 1);
    }

    /**
     * 请求建立链接超时设置 响应超时设置
     *
     * @author zhanggk
     */
    @Bean
    Request.Options feignOptions() {
        return new Request.Options(200, 30000);
    }
}
