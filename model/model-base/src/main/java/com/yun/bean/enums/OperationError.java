package com.yun.bean.enums;

import com.yun.bean.exception.ErrorType;
import lombok.Getter;

@Getter
public enum OperationError implements ErrorType {

    // 请求用户 050
    UNSUPORT_OPERATION("050400", "不支持操作"),
    UNAUTHORIZED("050401", "无权限"),
    ILLEGAL_REQUEST("050402", "非法请求"),
    UN_LOGIN("050403", "未登录"),
    LOGIN_EXPIRE("050404", "登录已过期，请重新登录"),
    USERNAME_EXIST("050405", "用户名已存在"),
    USERNAEM_ERROR("050406", "账号不存在"),
    PASSWD_ERROR("050407", "密码错误"),
    PASSWD_CIPHERTEXT_ERROR("050408", "密码密文错误，请重新输入..."),
    VERIFICATION_CODE("050409", "验证码输入错误"),
    PASSWORD_FORMAT_ERROR("050410", "密码格式错误"),
    USER_NON_ENABLED("050411", "账号未启用，请联系管理员"),
    USER_LOCKED("050412", "账号被锁定，请联系管理员"),
    USER_EXPIRED("050413", "账号过期，请联系管理员"),
    USER_CRE_EXPRIRED("050414", "密码过期失效，请修改密码后重新登录"),
    MAIL_NOT_NULL("050415", "邮箱为空"),
    USERNAME_NOT_NULL("050416", "用户名不能为空"),
    PASSWORD_NOT_NULL("050417", "密码不能为空"),
    All_NULL("050418", "请填写完整信息"),
    MAIL_FORMAT_ERROR("050419", "邮箱格式错误"),
    MAIL_CODE_TIME_OUT("050420", "邮箱验证码超时"),
    MAIL_CODE_ERROE("050421", "邮箱验证码错误"),
    MAIL_CODE_NULL("050422", "邮箱验证码不能为空"),

    // 项目 060
    SUBMIITED("060400", "项目已提交"),
    PRO_NON_EXIST("060401", "项目不存在"),
    UNSUPORT_STATUS("060402", "项目状态码异常"),
    DEPART_ERROR("060403", "部门不存在");


    /**
     * 错误类型码
     */
    private String code;
    /**
     * 错误类型描述信息
     */
    private String msg;

    OperationError(String code, String mesg) {
        this.code = code;
        this.msg = mesg;
    }

    @Override
    public void setMsg(String msg) {
        this.msg = msg;
    }
}
