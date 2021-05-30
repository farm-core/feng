package com.yun.gateway.utils;

import com.yun.bean.gateway.GatewayRouteDTO;
import org.springframework.web.server.ServerWebExchange;

/**
 * @program: datacenter
 * @description: 网关路由工具类
 * @author: wxf
 * @date: 2020-05-20 10:38
 **/

public class GatewayRouteUtil {

    public static final String USER_GATEWAY_ROUTE_CACHE = "user_gateway_route_cache";
    public static final String USER_GATEWAY_ROUTE_RETRY = "user_gateway_route_retry";
    public static final String USER_GATEWAY_ROUTE_RETRY_MAX = "user_gateway_route_retry_max";
    public static final String USER_GATEWAY_ROUTE_RETRY_ALREADY = "user_gateway_route_retry_already";
    public static final String USER_GATEWAY_ROUTE_REDIRECT = "user_gateway_route_redirect";
    public static final String USER_GATEWAY_ROUTE = "user_gateway_route";

    public static void setUserRouteAlreadyRetry(ServerWebExchange exchange) {
        exchange.getAttributes().put(USER_GATEWAY_ROUTE_RETRY, true);
    }

    public static boolean isUserRouteAlreadyRetry(ServerWebExchange exchange) {
        return exchange.getAttributeOrDefault(USER_GATEWAY_ROUTE_RETRY, false);
    }

    public static int getUserRouteRetryMax(ServerWebExchange exchange) {
        return exchange.getAttributeOrDefault(USER_GATEWAY_ROUTE_RETRY_MAX, 1);
    }

    public static int getUserRouteRetryAlready(ServerWebExchange exchange) {
        return exchange.getAttributeOrDefault(USER_GATEWAY_ROUTE_RETRY_ALREADY, 0);
    }

    public static void setUserRouteAlreadyRedirect(ServerWebExchange exchange) {
        exchange.getAttributes().put(USER_GATEWAY_ROUTE_REDIRECT, true);
    }

    public static boolean isUserRouteAlreadyRedirect(ServerWebExchange exchange) {
        return exchange.getAttributeOrDefault(USER_GATEWAY_ROUTE_REDIRECT, false);
    }

    public static GatewayRouteDTO getUserRoute(ServerWebExchange exchange) {
        return exchange.getAttribute(USER_GATEWAY_ROUTE);
    }
}
