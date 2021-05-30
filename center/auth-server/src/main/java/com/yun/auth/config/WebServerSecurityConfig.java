package com.yun.auth.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @ClassName WebServerSecurityConfig
 * @Description 配置信息
 * @Auther wu_xufeng
 * @Date 2020/11/24
 * @Version 1.0
 */
@Slf4j
@Configuration
@EnableWebSecurity
public class WebServerSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * 官方提供
     */
    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * 请求拦截
     *
     * @param http
     * @throws Exception
     */
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        // csrf设置
//        http.cors().disable();
//
//        // 配置允许访问actuator节点，其余路径访问均需校验权限
//        http.httpBasic().and()
//                .authorizeRequests()
//                .requestMatchers(EndpointRequest.toAnyEndpoint())
//                .permitAll()
//                .and()
//                .authorizeRequests()
//                // 放行一下接口路径
//                .antMatchers(
//                        "/**/register", "/**/login", "/**/getVerifyCode",
//                        "/swagger-resources/**", "/swagger-ui.html/**",
//                        "/actuator/**", "/webjars/**", "/v2/**", "/error").permitAll()
//                .and()
//                // 登陆成功跳转展示页面
//                .formLogin().successForwardUrl("/user/list")
//                .and()
//                // 退出登陆跳转登陆页面
//                .logout().logoutUrl("/logout").logoutSuccessUrl("/login")
//                .and()
//                .authorizeRequests()
//                .anyRequest()
//                .authenticated();
//    }

    /**
     * 注入自定义的userDetailsService实现，获取用户信息，设置密码加密方式
     *
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    /**
     * 将 AuthenticationManager 注册为 bean , 方便配置 oauth server 的时候使用
     *
     * @return
     * @throws Exception
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * 密码使用BCryptPasswordEncoder加密
     *
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
