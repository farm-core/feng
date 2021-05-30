package com.yun.admin.global;

import com.yun.admin.exception.APIException;
import com.yun.admin.exception.OperationException;
import com.yun.bean.enums.OperationError;
import com.yun.bean.base.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @ClassName ExceptionControllerAdvice
 * @Description 统一异常捕捉
 * @Auther wu_xufeng
 * @Date 2020/11/9
 * @Version 1.0
 */
@Slf4j
@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(value = OperationException.class)
    public Result operationExceptionHandler(OperationException ex) {
        log.error("operation exception:{}", ex.getErrorType().getMsg());
        return Result.fail(ex.getErrorType());
    }

    @ExceptionHandler(value = APIException.class)
    public Result apiExceptionHandler(APIException ex) {
        log.error("operation exception:{}", ex.getMsg());
        return Result.fail(OperationError.ILLEGAL_REQUEST);
    }
}
