package com.yun.auth.costom;

import com.alibaba.fastjson.JSON;
import com.yun.bean.base.Result;
import com.yun.bean.exception.SystemErrorType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @ClassName MyBasicAuthenticationEntryPoint
 * @Description 自定义 basicAuthEntryPoint 用于捕捉鉴权异常  {@link AuthenticationException}
 * * 具体异常种类可以查看其子类；捕获鉴权异常后返回通用异常结果 {@link Result}
 * @Auther wu_xufeng
 * @Date 2020/11/24
 * @Version 1.0
 */
@Component
public class MyBasicAuthenticationEntryPoint extends BasicAuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json; charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter printWriter = new PrintWriter(response.getOutputStream());
        Result result = Result.fail(SystemErrorType.ACCOUNT_EXCEPTION);
        printWriter.write(JSON.toJSONString(result));
        printWriter.flush();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        setRealmName("developlee");
        super.afterPropertiesSet();
    }
}
