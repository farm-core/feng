package com.yun.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yun.auth.client.service.AuthService;
import com.yun.bean.auth.vo.AuthView;
import com.yun.bean.auth.vo.ParamView;
import com.yun.bean.exception.SystemErrorType;
import com.yun.bean.gateway.GatewayRouteDTO;
import com.yun.bean.result.Result;
import com.yun.feign.client.bean.MyException;
import com.yun.gateway.utils.GetDateTime;
import com.yun.gateway.utils.ObsTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.yun.gateway.utils.GatewayRouteUtil.*;

/**
 * 全局过滤器 用于权限判断
 *
 * @author wxf
 * @date 2019/5/24
 */
@Slf4j
@Order(-1)
@SuppressWarnings("unchecked")
@RefreshScope
@Configuration
public class AccessGatewayFilter implements GlobalFilter, Ordered {

    private final static String CLIENT_MODE = "client";
    private final static String OAUTH_MODE = "oauth";
    /**
     * 注入鉴权Feign客户端
     */
    @Autowired
    private AuthService authService;
    @Autowired
    private ArrayList<GatewayRouteDTO> gatewayRoutes;
    /*@Autowired
    GatewayRouteDTO defaultGatewayRoute;*/

    /**
     * @param exchange exchange
     * @param chain    chain
     * @return reactor.core.publisher.Mono<java.lang.Void>
     * @author wxf
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String method = request.getMethodValue();
        String url = request.getPath().value();
        log.info("URI:{}", request.getURI());
        log.info("url:{},method:{},headers:{}", url, method, request.getHeaders());
        //不需要网关签权的url
        if (authService.ignoreAuthentication(url)) {
            log.info("访问链接无需鉴权：{}", url);
            return chain.filter(exchange);
        }
        //调用鉴权服务
        String mode = checkAuthenMode(request);
        Result result;
        switch (mode) {
            case CLIENT_MODE:
                result = authService.authenticate(request.getQueryParams().getFirst("adm"), request.getQueryParams().getFirst("pwd"), url, method);
                break;
            case OAUTH_MODE:
                result = authService.authenticate(request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION), url, method);
                break;
            default:
                return unauthorized(exchange, Result.fail(SystemErrorType.UNAUTHORIZED));

        }

        GatewayRouteDTO gatewayRoute;
        if (result.validate()) {
            AuthView authView = (AuthView) result.getData();
            List<ParamView> paramViews = authView.getApiView().getParams();
            log.debug("params:{}", paramViews);
            boolean flag = true;
            for (ParamView paramView : paramViews) {
                List<String> strings = request.getQueryParams().get(paramView.getParamName());
                if (strings == null || strings.isEmpty()) {
                    flag = paramView.getEnableBlank();
                    if (!flag) {
                        result = Result.fail(SystemErrorType.ARGUMENT_NOT_VALID);
                        break;
                    }
                } else {
                    flag = paramView.compare(strings.get(0));
                    if (!flag) {
                        result = Result.fail(SystemErrorType.ARGUMENT_NOT_VALID);
                        break;
                    }
                }
            }

            // 鉴权通过后，增加路由规则信息
            if (flag) {
                gatewayRoute = gatewayRoutes.stream()
                        .filter(r -> r.getApiId().equals(authView.getApiView().getId()) && r.getUserId().equals(authView.getUserview().getId()))
                        .findAny()
                        .orElse(null);
                log.debug("用户route {}", gatewayRoute);
                if (gatewayRoute != null) {
                    userRouteDefine(exchange, gatewayRoute);
                }
                return chain.filter(exchange);
            }
        }

        return unauthorized(exchange, result);
    }


    /**
     * 获取默认路由规则
     * @return GatewayRouteDTO
     * @author wxf
     */
    /*private GatewayRouteDTO defaultGatewayRoute() {
        return defaultGatewayRoute;
    }*/

    /**
     * @param serverWebExchange 请求上下文
     * @param result            鉴权信息
     * @return reactor.core.publisher.Mono
     * @author wxf
     */
    private Mono<Void> unauthorized(ServerWebExchange serverWebExchange, Result result) {
        serverWebExchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        HttpHeaders headers = serverWebExchange.getResponse().getHeaders();
        try {
            headers.add(HttpHeaders.CONTENT_TYPE, "application/json; charset=utf-8");
            headers.add("result-record", "unauthorized");
        } catch (UnsupportedOperationException e) {
            log.debug("ReadOnlyHttpHeaders bug");
        }
        DataBuffer buffer = serverWebExchange.getResponse().bufferFactory()
                .wrap(JSON.toJSONString(result).getBytes());
        return serverWebExchange.getResponse().writeWith(Flux.just(buffer));
    }

    /**
     * 判断请求鉴权模式
     *
     * @param request org.springframework.http.server.reactive.ServerHttpRequest
     * @return java.lang.String
     * @author wxf
     * @date 2019/6/3 16:13
     */
    private String checkAuthenMode(ServerHttpRequest request) {
        MultiValueMap<String, String> queryParams = request.getQueryParams();
        String username = queryParams.getFirst("adm");
        String password = queryParams.getFirst("pwd");
        String authorization = request.getHeaders().getFirst("Authorization");
        if (username != null && password != null && authorization == null) {
            return CLIENT_MODE;
        }
        if (username == null && password == null && authorization != null) {
            return OAUTH_MODE;
        }
        return "";

    }

    /**
     * 根据用户路由信息，增加路由规则
     *
     * @param gatewayRoute 路由信息
     * @author wxf
     */
    private void userRouteDefine(ServerWebExchange exchange, GatewayRouteDTO gatewayRoute) {
        Map<String, Object> attributes = exchange.getAttributes();
        attributes.put(USER_GATEWAY_ROUTE, gatewayRoute);

        MultiValueMap<String, String> queryParams = exchange.getRequest().getQueryParams();

        String obsTime1 = null;
        String obsTime2 = null;
        String sta = null;
        String city = null;
        String defultCont = null;
        long count = 0;
        String pageNum = queryParams.getFirst("pageNum");
        if (StringUtils.isNotBlank(pageNum) && gatewayRoute.getPageSize() != null) {
            URI uri = exchange.getRequest().getURI();
            StringBuilder query = new StringBuilder();
            String originalQuery = uri.getRawQuery();
            if (StringUtils.isNotBlank(originalQuery)) {
                query.append(originalQuery);
                if (originalQuery.charAt(originalQuery.length() - 1) != '&') {
                    query.append('&');
                }
            }
            // 重新封装上下文
            query.append("pageSize");
            query.append('=');
            query.append(gatewayRoute.getPageSize().toString());
            uri = UriComponentsBuilder.fromUri(exchange.getRequest().getURI()).replaceQuery(query.toString()).build(true).toUri();
            ServerHttpRequest directReq = exchange.getRequest().mutate().uri(uri).build();
            exchange = exchange.mutate().request(directReq).response(exchange.getResponse()).build();
        }
        if (StringUtils.isNotBlank(gatewayRoute.getFields())) {
            JSONObject jsonObject = JSONObject.parseObject(gatewayRoute.getFields());
            obsTime1 = queryParams.getFirst(jsonObject.getString("startTime"));
            obsTime2 = queryParams.getFirst(jsonObject.getString("endTime"));
            if (StringUtils.isBlank(obsTime1) && StringUtils.isBlank(obsTime2)) {
                obsTime1 = queryParams.getFirst(jsonObject.getString("obsTime"));
                obsTime2 = queryParams.getFirst(jsonObject.getString("obsTime"));
            }
            sta = queryParams.getFirst(jsonObject.getString("sta"));
            city = queryParams.getFirst(jsonObject.getString("city"));
            defultCont = jsonObject.getString("defultCont");
            if (StringUtils.isNotBlank(sta) && StringUtils.isNotBlank(city)) {
                if (sta.split(",").length >= city.split(",").length) {
                    count = sta.split(",").length;
                } else {
                    count = city.split(",").length;
                }
            } else if (StringUtils.isNotBlank(sta) && StringUtils.isBlank(city)) {
                count = sta.split(",").length;
            } else if (StringUtils.isBlank(sta) && StringUtils.isNotBlank(city)) {
                count = city.split(",").length;
            } else if (StringUtils.isBlank(sta) && StringUtils.isBlank(city)) {
                count = Long.valueOf(defultCont);
            }
        }

        Date startTime = null;
        Date endTime = null;

        if (StringUtils.isNotBlank(obsTime1)) {
            startTime = ObsTimeUtil.parseDate(obsTime1);
        }

        if (StringUtils.isNotBlank(obsTime2)) {
            endTime = ObsTimeUtil.parseDate(obsTime2);
        }

        Date maxObsTime = gatewayRoute.getMaxObsTime();
        if (maxObsTime == null) {
            maxObsTime = new Date();
        }

        if (gatewayRoute.getObsTimeRange() != null || gatewayRoute.getDataSize() != null) {
            // 抛出自定义异常直接返回
            long huor = GetDateTime.getDatePoor(endTime, startTime, "hour");
            count = huor * count;
            long day = GetDateTime.getDatePoor(endTime, startTime, "day");
            if (day > gatewayRoute.getObsTimeRange()) {
                throw new MyException("当前限制接口请求时间长度为：" + gatewayRoute.getObsTimeRange() + "天");
            } else if (count > gatewayRoute.getDataSize()) {
                throw new MyException("当前限制接口请求数据量为：" + gatewayRoute.getDataSize() + "条");
            }
        }

        if (startTime != null && gatewayRoute.getMinObsTime().getTime() > startTime.getTime()
                || endTime != null && maxObsTime.getTime() < endTime.getTime()) {
            attributes.put(USER_GATEWAY_ROUTE_REDIRECT, false);
        } else {
            setUserRouteAlreadyRedirect(exchange);
        }

        if (!gatewayRoute.getRedirectEnable()) {
            setUserRouteAlreadyRedirect(exchange);
        }

        if (!gatewayRoute.getRetryEnable()) {
            setUserRouteAlreadyRetry(exchange);
        }

    }

    @Override
    public int getOrder() {
        return 5;
    }
}
