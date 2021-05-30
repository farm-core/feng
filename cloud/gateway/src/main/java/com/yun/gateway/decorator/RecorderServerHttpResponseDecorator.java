package com.yun.gateway.decorator;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yun.bean.annotation.RunTimeCount;
import com.yun.bean.gateway.GatewayRouteDTO;
import com.yun.common.utils.HttpClientUtils;
import org.jsoup.Connection;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @program: datacenter
 * @description: 网关转发后返回数据处理(后续进行重定向)
 * @author: wxf
 * @create: 2019-08-02 09:21
 **/
public class RecorderServerHttpResponseDecorator extends ServerHttpResponseDecorator {
    private int length;
    private URI url;
    private ServerHttpRequest request;

    @Autowired
    ArrayList<GatewayRouteDTO> gatewayRoutes;

    public RecorderServerHttpResponseDecorator(ServerHttpResponse delegate, URI url, ServerHttpRequest request) {
        super(delegate);
        this.url = url;
        this.request = request;
    }

    @Override
    @RunTimeCount
    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
        DataBufferFactory bufferFactory = getDelegate().bufferFactory();
        StringBuilder builder = new StringBuilder();
        if (body instanceof Flux) {
            Flux<? extends DataBuffer> fluxBody = (Flux<? extends DataBuffer>) body;
            Flux<DataBuffer> flux = fluxBody.map(dataBuffer -> {
                // probably should reuse buffers
                byte[] content = new byte[dataBuffer.readableByteCount()];
                dataBuffer.read(content);
                String responseResult = new String(content, Charset.forName("UTF-8"));
                builder.append(responseResult);
                long count = builder.chars().filter(c -> c == '{').count();
                long count2 = builder.chars().filter(c -> c == '}').count();
                if (count == count2) {
                    JSONObject jsonObject = JSON.parseObject(builder.toString());
                    if ("200".equals(jsonObject.getString("status"))
                            && ("[]".equals(jsonObject.getString("content"))
                            || !jsonObject.containsKey("content"))) {

                        MultiValueMap<String, String> queryParams = request.getQueryParams();
                        String username = queryParams.getFirst("adm");
                        String path = request.getPath().subPath(2).value();
                        String host = "61.50.111.214";
                        String port = "19008";
                        for (GatewayRouteDTO gatewayRoute : gatewayRoutes) {
                            if (gatewayRoute.getOriginUrl().equals(path) && username.equals(gatewayRoute.getUsername())) {
                                host = gatewayRoute.getRedirectUrl();
                                port = gatewayRoute.getPort();
                                break;
                            }
                        }

                        url = UriComponentsBuilder.fromUri(url).host(host).port(port).build().toUri();
                        try {
                            Connection.Response res = HttpClientUtils.getContent(url.toString(), new HashMap<>(), Connection.Method.GET);
                            content = res.body().getBytes("UTF-8");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                return bufferFactory.wrap(content);
            });
            return super.writeWith(flux);
        }
        return super.writeWith(body); // if body is not a flux. never got there.
    }
}
