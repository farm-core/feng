package com.yun.admin.interceptor;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yun.admin.context.UserContext;
import com.yun.bean.admin.YunUser;
import com.yun.bean.enums.OperationError;
import com.yun.bean.exception.ErrorType;
import com.yun.bean.base.Result;
import com.yun.idb.mapper.YunUserMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.MacSigner;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName TokenInterceptor
 * @Description token校验 用户登录拦截器
 * @Auther wu_xufeng
 * @Date 2020/11/9
 * @Version 1.0
 */
public class TokenInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private YunUserMapper yunUserMapper;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String url = request.getRequestURL().toString();
        String token = request.getHeader("access_token");
        // 不是登陆/注册接口token不能为空
        if(StringUtils.isBlank(token) && !url.contains("login") && !url.contains("register") && !url.contains("getVerifyCode")){
            errorReturn(response, OperationError.UN_LOGIN);
            return false;
        }

        MacSigner macSigner = new MacSigner("yun");
        String claims = null;
        try {
            claims = JwtHelper.decodeAndVerify(token, macSigner).getClaims();
        } catch (Exception e) {
            errorReturn(response,OperationError.UN_LOGIN);
            return false;
        }

        String[] split = claims.split(":");
        Boolean member = stringRedisTemplate.opsForSet().isMember(split[0], token);
        if (member) {
            stringRedisTemplate.expire(split[0], 30, TimeUnit.MINUTES);
        } else {
            errorReturn(response,OperationError.LOGIN_EXPIRE);
            return false;
        }
        QueryWrapper<YunUser> wrapper = new QueryWrapper<>();
        wrapper.eq("user_name", split[0]);
        YunUser yunUser = yunUserMapper.selectOne(wrapper);
        UserContext.getInstance().set(yunUser);
        return member;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserContext.getInstance().remove();
    }

    /**
     * 拦截回执数据
     *
     * @param response
     * @param errorType
     */
    private void errorReturn(HttpServletResponse response, ErrorType errorType) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        try (PrintWriter writer = response.getWriter()) {
            Result fail = Result.fail(errorType);
            String res = JSON.toJSONString(fail);
            writer.print(res);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
