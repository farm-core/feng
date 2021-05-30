/*
package com.yun.gateway.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.config.PropertiesRouteDefinitionLocator;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

*/
/**
 * @program: datacenter
 * @description: 动态路由资源
 * @author: zhanggk
 * @date: 2020-05-19 15:45
 **//*



@Component
@Slf4j
public class DynamicRouteDefinitionRepository implements RouteDefinitionRepository {
    @Autowired
    PropertiesRouteDefinitionLocator routeDefinitionLocator;

    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {
        log.debug("dynamicRoute : 获取路由表信息");
        return routeDefinitionLocator.getRouteDefinitions();
    }

    @Override
    public Mono<Void> save(Mono<RouteDefinition> route) {
        return null;
    }

    @Override
    public Mono<Void> delete(Mono<String> routeId) {
        return null;
    }
}
*/
