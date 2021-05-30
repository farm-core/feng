package com.yun.bean.exception;

import lombok.Getter;

@Getter
public enum SystemErrorType implements ErrorType {

    SYSTEM_ERROR("-1", "系统异常"),
    SYSTEM_BUSY("000001", "系统繁忙,请稍候再试"),

    //010服务级码
    GATEWAY_NOT_FOUND_SERVICE("010404", "服务未找到"),
    GATEWAY_ERROR("010500", "网关异常"),
    GATEWAY_CONNECT_TIME_OUT("010002", "网关超时"),

    // 020接口级码
    ARGUMENT_NOT_VALID("020000", "请求参数校验不通过"),
    URL_NOT_FOUND("020100","请求连接未找到"),
    API_NOT_FOUND("020404","请求接口不存在"),
    UNAUTHORIZED("020401","无接口访问权限"),
    ACCOUNT_EXCEPTION("010402","账号密码错误"),
    TOO_MUCH_DATA_EXCEPTION("030401","请求数据量过大"),
    REQUEST_IS_FREQUENT("030403","请求过于频繁请稍后重试");


    /**
     * 错误类型码
     */
    private String code;
    /**
     * 错误类型描述信息
     */
    private String msg;

    SystemErrorType(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public void setMsg(String msg) {
        this.msg = msg;
    }
}
