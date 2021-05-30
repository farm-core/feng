package com.yun.admin.exception;

import com.yun.bean.enums.OperationError;
import com.yun.bean.exception.ErrorType;
import lombok.Getter;

/**
 * 自定义操作异常
 */
@Getter
public class OperationException extends RuntimeException {
    /**
     * 异常对应的错误类型
     */
    private final ErrorType errorType;

    /**
     * 默认是系统异常
     */
    public OperationException() {
        this.errorType = OperationError.UNSUPORT_OPERATION;
    }

    public OperationException(ErrorType errorType) {
        this.errorType = errorType;
    }

    public OperationException(ErrorType errorType, String message) {
        super(message);
        this.errorType = errorType;
    }

    public OperationException(ErrorType errorType, String message, Throwable cause) {
        super(message, cause);
        this.errorType = errorType;
    }
}
