package com.yun.feign.client.service;

import com.yun.feign.client.service.impl.FeignAdminInfoServiceImpl;
import com.yun.bean.admin.YunUser;
import com.yun.bean.base.Result;
import com.yun.bean.gateway.GatewayRoute;
import com.yun.bean.gateway.GatewayRouteDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

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
@FeignClient(value = "admin", path = "/admin", fallback = FeignAdminInfoServiceImpl.class)
public interface FeignAdminInfoService {

    @GetMapping("/yun-user/getByUsername")
    Result<YunUser> getByUsername(@RequestParam("username") String username);

    @GetMapping("/route/all")
    Result<List<GatewayRouteDTO>> routeList();

    @GetMapping("/route/query")
    Result<List<GatewayRoute>> queryAllRoutes();
}
