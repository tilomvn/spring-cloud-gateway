package com.spring.cloud.gateway.routers;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.gateway.filter.factory.RetryGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ServerWebExchange;

class MyRetryGatewayFilterFactory extends RetryGatewayFilterFactory {

    final RestTemplate restTemplate;

    MyRetryGatewayFilterFactory(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @Override
    public void reset(ServerWebExchange exchange) {
        super.reset(exchange);
        //TODO: modify things before re-send
        ServerHttpRequest.Builder newRequest = exchange.getRequest().mutate();
        newRequest.header(HttpHeaders.AUTHORIZATION, "Bearer asdkhasd.askdhkuasd");
        exchange.mutate().request(newRequest.build());
    }
}
