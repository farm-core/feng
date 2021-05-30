package com.yun.feign.client.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.yun.bean.exception.SystemErrorType;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Created by Morange on 2018/2/2.
 */
@Getter
public class Result<T> {

    private int status = HttpStatus.OK.value();

    private String msg = "success";

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T content;

    public Result(int status, String msg, T content) {
        this.status = status;
        this.msg = msg;
        this.content = content;
    }

    public Result(int status, T content) {
        this.status = status;
        this.content = content;
    }

    public Result(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public Result(T content) {
        this.content = content;
    }

    public static Result success(Object data) {
        return new Result(data);
    }

    public static Result fail() {
        return new Result(SystemErrorType.SYSTEM_ERROR);
    }

}
