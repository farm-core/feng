package com.yun.gateway.config;

import com.yun.bean.gateway.GatewayRouteDTO;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;

/**
 * @program: datacenter
 * @description:
 * @author: wxf
 * @date: 2020-05-20 15:38
 **/
@Configuration
public class CacheManageConfig {

    @Bean
    public ArrayList<GatewayRouteDTO> gatewayRoutes() {
        return new ArrayList<>();
    }

    /*@Bean
    public GatewayRouteDTO defaultGatewayRoute() {
        LocalDateTime time1 = LocalDateTime.of(2020, 6, 1, 0, 0, 0);
        ZoneId zoneId = ZoneId.systemDefault();
        Date date1 = Date.from(time1.atZone(zoneId).toInstant());
        return GatewayRouteDTO.builder()
                .id(100L)
                .enable(true)
                .redirectEnable(true)
                .retryEnable(false)
                .redirectUrl("61.50.111.214")
                .port("19008")
                .maxRetry(1)
                .username("system")
                .routeOrder(10)
                .minObsTime(date1)
                .build();
    }*/


}
