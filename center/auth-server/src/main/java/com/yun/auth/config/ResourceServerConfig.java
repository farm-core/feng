package com.yun.auth.config;

import com.yun.auth.costom.MyBasicAuthenticationEntryPoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

/**
 * @ClassName ResourceServerConfig
 * @Description 资源管理器
 * * spring security oauth2 配置信息
 * * 服务中另外配置了spring security 配置信息 {@link WebServerSecurityConfig}
 * * 两者的关系，由于oauth2过滤器执行顺序更靠前，会覆盖spring security的部分配置
 * @Auther wu_xufeng
 * @Date 2020/11/24
 * @Version 1.0
 */
@Slf4j
@RefreshScope
@Configuration
@EnableResourceServer
//@EnableGlobalMethodSecurity(prePostEnabled = true) //方法执行前验证
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    /**
     * 自定义权限异常请求entryPoint，鉴权失败后返回通用请求结果
     */
    @Autowired
    private MyBasicAuthenticationEntryPoint basicAuthenticationEntryPoint;

    /**
     * tokenStore 令牌持久化方式，JWT存储，也可以使用database，或者内存存储
     * 具体实现 {@link JwtTokenStore}
     */
    @Autowired
    private TokenStore tokenStore;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.tokenStore(tokenStore)
                .resourceId("WEBS");
        ;
    }

    /**
     * oauth2过滤器执行规则
     *
     * @author wxf
     * @date 2020/6/8 17:46
     * @since 2020/6/17 增加/oauth/**的过滤规则
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        log.debug("ResourceServerConfig 拦截器配置");
        http.csrf().disable();
//        允许所有actuator请求通过
        http.httpBasic().authenticationEntryPoint(basicAuthenticationEntryPoint)
                .and()
                .authorizeRequests()
                .requestMatchers(EndpointRequest.toAnyEndpoint()).permitAll()
                .and()
                .authorizeRequests().antMatchers("/druid/**").permitAll()
                .and()
                .authorizeRequests().antMatchers("/oauth/**").permitAll()
                .and()
                .authorizeRequests().antMatchers("/route/**").permitAll()
                .and()
                .authorizeRequests().antMatchers("/actuator/*").permitAll()
                .and()
                .authorizeRequests()
                .anyRequest().authenticated();
    }
}
