package com.yun.auth.service.impl;

import brave.Tracer;
import com.alibaba.fastjson.JSON;
import com.yun.auth.service.YunApiService;
import com.yun.bean.admin.YunApi;
import com.yun.bean.admin.YunUser;
import com.yun.feign.client.service.FeignAdminInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @ClassName UserDetailsServiceImpl
 * @Description 自定义实现用户权限接口
 * @Auther wu_xufeng
 * @Date 2020/11/24
 * @Version 1.0
 */
@Slf4j
@Service("UserDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {
    /**
     * zipkin追踪
     */
    @Autowired
    private Tracer tracer;

    @Autowired
    private FeignAdminInfoService feignAdminInfoService;

    @Autowired
    private YunApiService yunApiService;

    /**
     * 用户权限
     *
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("请求授权的用户:{}", username);
        YunUser yunUser = feignAdminInfoService.getByUsername(username).getData();
        if (Objects.isNull(yunUser)) {
            throw new UsernameNotFoundException("用户不存在");
        }

        log.debug("loadByUsername:{}", yunUser.toString());

        // 记录信息
        tracer.currentSpan().tag("username", username);

        // 返回用户信息
        return new User(
                username,
                yunUser.getPassword(),
                yunUser.getEnabled(),
                yunUser.getAccountNonExpired(),
                yunUser.getCredentialsNonExpired(),
                yunUser.getAccountNonLocked(),
                this.obtainGrantedAuthorities(yunUser));
    }

    /**
     * 添加接口信息或者其他的信息
     *
     * @param yunUser
     * @return
     */
    private Set<GrantedAuthority> obtainGrantedAuthorities(YunUser yunUser) {
        Set<YunApi> apiEntities = yunApiService.queryUserApisByUserId(yunUser.getId());
        return apiEntities.stream()
                .map(apiEntity -> new SimpleGrantedAuthority(JSON.toJSONString(apiEntity)))
                .collect(Collectors.toSet());
    }
}
