package com.yun.gateway.handler;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

/**
 * 将定义好的路由表信息通过此类读写到redis中
 */
@Component
public class RedisRouteDefinitionRepository implements RouteDefinitionRepository {

    public static final String GATEWAY_ROUTES = "gateway:routes";
    private List<RouteDefinition> routeDefinitionList;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public RedisRouteDefinitionRepository(List<RouteDefinition> routeDefinitionList) {
        this.routeDefinitionList = routeDefinitionList;
    }

    /**
     * 从redis取路由信息的方法
     *
     * @return
     */
    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {
        List<RouteDefinition> routeDefinitions = new ArrayList<>();
        try {
            redisTemplate.opsForHash().values(GATEWAY_ROUTES).stream().forEach(routeDefinition -> {
                routeDefinitions.add(JSON.parseObject(routeDefinition.toString(), RouteDefinition.class));
            });
            return Flux.fromIterable(routeDefinitions);
        } catch (Exception e) {
            return Flux.fromIterable(routeDefinitionList);
        }
    }

    @Override
    public Mono<Void> save(Mono<RouteDefinition> route) {
        try {
            return route
                    .flatMap(routeDefinition -> {
                        redisTemplate.opsForHash().put(GATEWAY_ROUTES, routeDefinition.getId(),
                                JSON.toJSONString(routeDefinition));
                        return Mono.empty();
                    });
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Mono<Void> delete(Mono<String> routeId) {
        try {
            return routeId.flatMap(id -> {
                if (redisTemplate.opsForHash().hasKey(GATEWAY_ROUTES, id)) {
                    redisTemplate.opsForHash().delete(GATEWAY_ROUTES, id);
                    return Mono.empty();
                }
                return Mono.defer(() -> Mono.error(new NotFoundException("路由文件没有找到: " + routeId)));
            });
        } catch (Exception e) {
            return Mono.defer(() -> Mono.error(new NotFoundException("路由文件没有找到: " + routeId)));
        }
    }
}
