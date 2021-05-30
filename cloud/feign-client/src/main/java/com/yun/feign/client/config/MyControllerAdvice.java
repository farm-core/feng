package com.yun.feign.client.config;

import com.alibaba.fastjson.JSON;
import com.yun.feign.client.bean.MyException;
import com.yun.feign.client.bean.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * Created by Morange on 2018/2/2.
 */
@Slf4j
@RestControllerAdvice
public class MyControllerAdvice {
    /**
     * 全局异常捕捉处理
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    public String errorHandler(Exception ex) {
        //记录错误信息
        log.error(ExceptionUtils.getFullStackTrace(ex));
        Result result = new Result(-1, ex.getMessage());
        return JSON.toJSONString(result);
    }

    /**
     * 拦截捕捉自定义异常 MyException.class
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(value = MyException.class)
    public String myErrorHandler(MyException ex) {
        Result result = new Result(ex.getCode(), ex.getMsg());
        return JSON.toJSONString(result);
    }
}
