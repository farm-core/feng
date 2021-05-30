package com.yun.admin.config;

import com.yun.admin.interceptor.TokenInterceptor;
import com.yun.admin.interceptor.UserInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName MvcConfig
 * @Description 请求拦截器
 * @Auther wu_xufeng
 * @Date 2020/11/9
 * @Version 1.0
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {

    /**
     * 添加用户信息
     *
     * @return
     */
    @Bean
    public HandlerInterceptor getUserInterceptor() {
        return new UserInterceptor();
    }

    @Bean
    public HandlerInterceptor getTokenInterceptor() {
        return new TokenInterceptor();
    }


    /**
     * 放行接口
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // addPathPatterns 用于添加拦截规则, 这里假设拦截 /url 后面的全部链接
        List<String> includePathPatterns = new ArrayList<>();
        includePathPatterns.add("/**");

        // excludePathPatterns 用户排除拦截
        registry.addInterceptor(getTokenInterceptor())
                .addPathPatterns(includePathPatterns)
                .excludePathPatterns(
                        // 注册/登录/验证码接口
                        "/**/register", "/**/login", "/**/getVerifyCode","/**",
                        // swagger的内置接口
                        "/swagger-resources/**", "/swagger-ui.html/**",
                        "/actuator/**", "/webjars/**", "/v2/**", "/error","/doc.html");
        registry.addInterceptor(getUserInterceptor()).addPathPatterns(includePathPatterns);
    }
}
