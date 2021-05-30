package com.yun.bean.exception;

public interface ErrorType {
    /**
     * 返回code
     *
     * @return
     */
    String getCode();

    /**
     * 返回msg
     *
     * @return
     */
    String getMsg();

    /**
     * 设置msg
     *
     * @return
     */
    void setMsg(String msg);
}
