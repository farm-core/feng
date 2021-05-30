package com.yun.gateway.filter;

import com.yun.gateway.decorator.RecorderServerHttpRequestDecorator;
import com.yun.gateway.decorator.RecorderServerHttpResponseDecorator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 日志处理
 */
@Slf4j
@Configuration
@SuppressWarnings("unchecked")
public class AccessLogGlobalFilter implements GlobalFilter, Ordered {

    private static final String REQUEST_PREFIX = "Request Info [ ";

    private static final String REQUEST_TAIL = " ]";

    private static final String RESPONSE_PREFIX = "Response Info [ ";

    private static final String RESPONSE_TAIL = " ]";

    private StringBuilder normalMsg = new StringBuilder();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        RecorderServerHttpRequestDecorator requestDecorator = new RecorderServerHttpRequestDecorator(request);
        InetSocketAddress address = requestDecorator.getRemoteAddress();
        HttpMethod method = requestDecorator.getMethod();
        URI url = requestDecorator.getURI();
        HttpHeaders headers = requestDecorator.getHeaders();
        Flux<DataBuffer> body = requestDecorator.getBody();
        //读取requestBody传参
        AtomicReference<String> requestBody = new AtomicReference<>("");
        body.subscribe(buffer -> {
            CharBuffer charBuffer = StandardCharsets.UTF_8.decode(buffer.asByteBuffer());
            requestBody.set(charBuffer.toString());
        });
        String requestParams = requestBody.get();
        normalMsg.append(REQUEST_PREFIX);
        normalMsg.append(";header=").append(headers);
        normalMsg.append(";params=").append(requestParams);
        normalMsg.append(";address=").append(address.getHostName() + address.getPort());
        normalMsg.append(";method=").append(method.name());
        normalMsg.append(";url=").append(url.getPath());
        normalMsg.append(REQUEST_TAIL);

        ServerHttpResponse response = exchange.getResponse();

        normalMsg.append(RESPONSE_PREFIX);

        RecorderServerHttpResponseDecorator decoratedResponse = new RecorderServerHttpResponseDecorator(response, url, request);

        return chain.filter(exchange.mutate().request(requestDecorator).response(decoratedResponse).build());
    }

    @Override
    public int getOrder() {
        return -3;
    }

}
