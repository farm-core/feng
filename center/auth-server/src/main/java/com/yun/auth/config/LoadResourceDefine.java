package com.yun.auth.config;

import com.alibaba.fastjson.JSON;
import com.yun.auth.service.YunApiService;
import com.yun.bean.admin.YunApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 项目启动对权限资源初始化类
 *
 * @author wxf
 * @date 2019/5/7
 * @see HandlerMappingIntrospector HandlerMapping集合工具类
 */
@Slf4j
@Component
class LoadResourceDefine {
    private YunApiService apiService;

    private HandlerMappingIntrospector mvcHandlerMappingIntrospector;

    public LoadResourceDefine(YunApiService apiService, HandlerMappingIntrospector mvcHandlerMappingIntrospector) {
        this.apiService = apiService;
        this.mvcHandlerMappingIntrospector = mvcHandlerMappingIntrospector;
    }

    @Bean
    public Map<RequestMatcher, ConfigAttribute> resourceConfigAttributes() {
        Set<YunApi> interfaceEntities = apiService.findAll();
        Map<RequestMatcher, ConfigAttribute> map = interfaceEntities.stream()
                .collect(Collectors.toMap(
                        interfaceEntity -> {
                            MvcRequestMatcher mvcRequestMatcher = new MvcRequestMatcher(mvcHandlerMappingIntrospector, interfaceEntity.getUrl());
                            mvcRequestMatcher.setMethod(HttpMethod.resolve(interfaceEntity.getType()));
                            return mvcRequestMatcher;
                        },
                        authEntity -> new SecurityConfig(JSON.toJSONString(authEntity))
                        )
                );
        log.debug("authConfigAttributes:{}", map);
        return map;
    }
}
