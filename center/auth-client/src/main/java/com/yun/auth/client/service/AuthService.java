package com.yun.auth.client.service;

import com.yun.bean.result.Result;

public interface AuthService {
    /**
     * oauth2鉴权模式，判断用户是否有权限
     *
     * @param authentication accesstoken oauth令牌
     * @param url            请求路径
     * @param method         请求方式
     * @return Result
     */
    Result authenticate(String authentication, String url, String method);

    /**
     * 判断url是否在忽略的范围内
     * 只要是配置中的开头，即返回true
     *
     * @param url 请求路径
     * @return com/clear/auth/client/service/AuthService.java:24
     */
    boolean ignoreAuthentication(String url);

    /**
     * 查看签权服务器返回结果，有权限返回true
     *
     * @param authResult 鉴权服务返回结果
     * @return com/clear/auth/client/service/AuthService.java:24
     */
    boolean hasPermission(Result authResult);

    /**
     * 调用签权服务，判断用户是否有权限
     *
     * @param authentication accesstoken oauth令牌
     * @param url            请求路径
     * @param method         请求方式
     * @return true/false
     */
    boolean hasPermission(String authentication, String url, String method);

    /**
     * 账号密码验权模式
     *
     * @param username 用户名
     * @param password 密码
     * @param url      请求地址
     * @param method   请求方式
     * @return com.clear.bean.result.Result
     * @author zhanggk
     * @date 2019/6/3 16:05
     */
    Result authenticate(String username, String password, String url, String method);
}
