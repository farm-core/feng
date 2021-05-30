package com.yun.feign.client.service.impl;

import com.yun.bean.admin.YunUser;
import com.yun.bean.base.Result;
import com.yun.bean.gateway.GatewayRoute;
import com.yun.bean.gateway.GatewayRouteDTO;
import com.yun.feign.client.service.FeignAdminInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class FeignAdminInfoServiceImpl implements FeignAdminInfoService {
    @Override
    public Result<YunUser> getByUsername(String username) {
        return null;
    }

    @Override
    public Result<List<GatewayRouteDTO>> routeList() {
        return null;
    }

    @Override
    public Result<List<GatewayRoute>> queryAllRoutes() {
        return null;
    }
}
