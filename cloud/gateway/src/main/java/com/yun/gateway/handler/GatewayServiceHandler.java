package com.yun.gateway.handler;

import com.alibaba.fastjson.JSONArray;
import com.yun.bean.gateway.GatewayRoute;
import com.yun.feign.client.service.FeignAdminInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class GatewayServiceHandler implements ApplicationEventPublisherAware, CommandLineRunner {
    @Autowired
    private RedisRouteDefinitionRepository routeDefinitionWriter;

    @Autowired
    private ApplicationEventPublisher publisher;

    @Autowired
    private FeignAdminInfoService feignAdminInfoService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }

    @Override
    public void run(String... args) throws Exception {
        this.loadRouteConfig();
    }

    public String loadRouteConfig() {
        log.info("====开始加载=====网关配置信息=========");
        //删除redis里面的路由配置信息
        try {
            redisTemplate.delete(RedisRouteDefinitionRepository.GATEWAY_ROUTES);
        } catch (Exception e) {
            List<GatewayRoute> gatewayRouteList = feignAdminInfoService.queryAllRoutes().getData();
            List<RouteDefinition> routeDefinitions = new ArrayList<>();
            if (!CollectionUtils.isEmpty(gatewayRouteList)) {
                gatewayRouteList.forEach(gatewayRoute -> {
                    RouteDefinition definition = handleData(gatewayRoute);
                    routeDefinitions.add(definition);
                });
            }
            new RedisRouteDefinitionRepository(routeDefinitions);

        } finally {
            //从数据库拿到基本路由配置
            List<GatewayRoute> gatewayRouteList = feignAdminInfoService.queryAllRoutes().getData();
            if (!CollectionUtils.isEmpty(gatewayRouteList)) {
                gatewayRouteList.forEach(gatewayRoute -> {
                    RouteDefinition definition = handleData(gatewayRoute);
                    routeDefinitionWriter.save(Mono.just(definition)).subscribe();
                });
            }

            this.publisher.publishEvent(new RefreshRoutesEvent(this));

            log.info("=======网关配置信息===加载完成======");
            return "success";
        }
    }

    public void saveRoute(GatewayRoute gatewayRoute) {
        RouteDefinition definition = handleData(gatewayRoute);
        routeDefinitionWriter.save(Mono.just(definition)).subscribe();
        this.publisher.publishEvent(new RefreshRoutesEvent(this));
    }

    public void update(GatewayRoute gatewayRoute) {
        RouteDefinition definition = handleData(gatewayRoute);
        try {
            this.routeDefinitionWriter.delete(Mono.just(definition.getId()));
            routeDefinitionWriter.save(Mono.just(definition)).subscribe();
            this.publisher.publishEvent(new RefreshRoutesEvent(this));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void deleteRoute(String routeId) {
        routeDefinitionWriter.delete(Mono.just(routeId)).subscribe();
        this.publisher.publishEvent(new RefreshRoutesEvent(this));
    }

    /**
     * 路由数据转换公共方法
     *
     * @param gatewayRoute
     * @return
     */
    private RouteDefinition handleData(GatewayRoute gatewayRoute) {
        RouteDefinition definition = new RouteDefinition();
        Map<String, String> predicateParams = new HashMap<>(8);
        List<PredicateDefinition> predicates = new ArrayList<>();
        List<FilterDefinition> filterDefinitions = new ArrayList<>();
//        Map<String, String> filterParams = new HashMap<>(8);

        URI uri = null;
        if (gatewayRoute.getUri().startsWith("http")) {
            //http地址
            uri = UriComponentsBuilder.fromHttpUrl(gatewayRoute.getUri()).build().toUri();
        } else {
            //注册中心
            uri = UriComponentsBuilder.fromUriString("lb://" + gatewayRoute.getUri()).build().toUri();
        }

        definition.setId(gatewayRoute.getServiceId());
        // 名称是固定的，spring gateway会根据名称找对应的PredicateFactory
        String predicateStr = gatewayRoute.getPredicates();
        String[] split = predicateStr.split(",");
        PredicateDefinition predicate = new PredicateDefinition();
        for (String pattern : split) {
            predicate = new PredicateDefinition();
            predicateParams = new HashMap<>(8);

            predicate.setName("Path");
            predicateParams.put("pattern", pattern);
            predicate.setArgs(predicateParams);

            predicates.add(predicate);
        }

        // 名称是固定的, 路径去前缀
        String filters = gatewayRoute.getFilters();
        List<Map<String, Map<String, Object>>> list = (List<Map<String, Map<String, Object>>>) JSONArray.parse(filters);
        FilterDefinition filterDefinition = new FilterDefinition();
        for (Map<String, Map<String, Object>> map : list) {
            for (String key : map.keySet()) {
                filterDefinition = new FilterDefinition();
                Map<String, Object> maps = map.get(key);
                for (String keys : maps.keySet()) {
                    if ("name".equals(keys)) {
                        filterDefinition.setName(maps.get(keys).toString());
                    } else {
                        filterDefinition.setArgs((Map<String, String>) maps.get(keys));
                    }
                }
                filterDefinitions.add(filterDefinition);
            }
        }

//        filterDefinition.setName("StripPrefix");
//        filterParams.put("_genkey_0", gatewayRoute.getFilters());
//        filterDefinition.setArgs(filterParams);

        definition.setPredicates(predicates);
        definition.setFilters(filterDefinitions);
        definition.setUri(uri);
        definition.setOrder(Integer.parseInt(gatewayRoute.getOrders()));

        return definition;
    }
}
