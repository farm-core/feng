package com.yun.admin.exception;

import com.yun.bean.exception.ErrorType;
import lombok.Getter;

/**
 * @ClassName APIExceptionHandler
 * @Description 自定义API异常
 * @Auther wu_xufeng
 * @Date 2020/11/9
 * @Version 1.0
 */
@Getter
public class APIException extends RuntimeException {
    private int code;
    private String msg;

    public APIException() {
        this(1001, "接口异常");
    }

    public APIException(String msg) {
        this(1001, msg);
    }

    public APIException(int code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public APIException(ErrorType errorType) {
        this.code = Integer.parseInt(errorType.getCode());
        this.msg = errorType.getMsg();
    }
}
