package com.yun.feign.client.service;

import com.yun.bean.result.Result;
import com.yun.feign.client.service.impl.FeignAuthInfoServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author wu_xufeng
 * @description 从ADMIN服务获取最新路由表信息
 * @since 2020-11-06
 */
@Component
@FeignClient(name = "auth-server", fallback = FeignAuthInfoServiceImpl.class)
public interface FeignAuthInfoService {

    @PostMapping(value = "/auth/permission")
    Result authClient(@RequestHeader(HttpHeaders.AUTHORIZATION) String authentication,
                                          @RequestParam("url") String url,
                                          @RequestParam("method") String method);

    @PostMapping(value = "/security/permission")
    Result authUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String authentication,
                    @RequestParam("url") String url,
                    @RequestParam("method") String method);
}
