package com.yun.auth.client.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.yun.auth.client.service.AuthService;
import com.yun.bean.auth.vo.AuthView;
import com.yun.bean.result.Result;
import com.yun.feign.client.service.FeignAuthInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

/**
 * 权限检验
 *
 * @author wxf
 */
@Slf4j
@Service
@RefreshScope
public class AuthServiceImpl implements AuthService {
    private static final String AUTH_REDIS_PREFIX = "auth_user_";

    private FeignAuthInfoService feignAuthInfoService;

    private StringRedisTemplate redisTemplate;
    /**
     * 不需要网关签权的url配置(/oauth,/open)
     * 默认/oauth开头是不需要的
     */
    private String ignoreUrls;

    public AuthServiceImpl(FeignAuthInfoService feignAuthInfoService, StringRedisTemplate redisTemplate,
                           @Value("${gate.ignore.authentication.startWith}") String ignoreUrls) {
        this.feignAuthInfoService = feignAuthInfoService;
        this.redisTemplate = redisTemplate;
        this.ignoreUrls = ignoreUrls;
    }

    @Override
    public Result authenticate(String authentication, String url, String method) {
        return feignAuthInfoService.authUser(authentication, url, method);
    }

    @Override
    public boolean ignoreAuthentication(String url) {
        log.debug("判断此次访问路径  {}", url);
        return Stream.of(this.ignoreUrls.split(",")).anyMatch(ignoreUrl -> url.startsWith(StringUtils.trim(ignoreUrl)));
    }

    @Override
    public boolean hasPermission(Result authResult) {
        return authResult.validate();
    }

    @Override
    public boolean hasPermission(String authentication, String url, String method) {
        return hasPermission(authenticate(authentication, url, method));
    }

    @Override
    public Result authenticate(String username, String password, String url, String method) {

        String params = (String) redisTemplate.opsForHash().get(AUTH_REDIS_PREFIX + username, url);
        if (StringUtils.isBlank(params)) {
            HttpHeaders headers = new HttpHeaders();
            headers.setBasicAuth(username, password);
            Result result = feignAuthInfoService.authUser(headers.getFirst(HttpHeaders.AUTHORIZATION), url, method);
            if (!result.validate()) {
                return result;
            }
            String jsonString = JSONArray.toJSONString(result.getData());
            redisTemplate.opsForHash().put(AUTH_REDIS_PREFIX + username, url, jsonString);
            redisTemplate.expire(username, 120, TimeUnit.SECONDS);
            return result;
        }

        AuthView authView = JSON.parseObject(params, AuthView.class);
        log.debug("用户权限资源缓存已加载：{}", authView);
        return Result.success(authView);
    }

}
