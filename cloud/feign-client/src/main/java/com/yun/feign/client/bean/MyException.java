package com.yun.feign.client.bean;

/**
 * Created by Morange on 2018/2/2.
 */
public class MyException extends RuntimeException {

    private Integer code = -1;
    private String msg;


    public MyException(String msg) {
        this.msg = msg;
    }

    public MyException() {
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
