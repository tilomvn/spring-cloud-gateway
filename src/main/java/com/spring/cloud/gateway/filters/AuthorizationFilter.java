package com.spring.cloud.gateway.filters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.cloud.gateway.common.AuthorizationService;
import com.spring.cloud.gateway.common.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import wiremock.org.eclipse.jetty.http.HttpHeader;

@Component
public class AuthorizationFilter implements GatewayFilter {

    Logger log = LoggerFactory.getLogger(AuthorizationFilter.class);

    final ObjectMapper objectMapper;
    final AuthorizationService authorizationService;

    AuthorizationFilter(ObjectMapper objectMapper, AuthorizationService authorizationService) {
        this.objectMapper = objectMapper;
        this.authorizationService = authorizationService;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        var headers = exchange.getRequest().getHeaders().getOrEmpty(HttpHeader.AUTHORIZATION.asString());
        if (headers.size() == 0) {
            return onError(exchange, HttpStatus.UNAUTHORIZED.value(), "Un-Authorized");
        } else {
            var jwtToken = headers.get(0);
            var authorizationResponse = authorizationService.authorize(jwtToken);
            if (!authorizationResponse.getAuthResult()) {
                return onError(exchange, HttpStatus.UNAUTHORIZED.value(), authorizationResponse.getMessage());
            }
            return chain.filter(exchange);
        }
    }

    Mono<Void> onError(ServerWebExchange exchange, int code, String errorMessage) {
        var response = exchange.getResponse();
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return response.writeWith(Mono.fromSupplier(() -> {
            var bufferFactory = response.bufferFactory();
            try {
                return bufferFactory.wrap(objectMapper.writeValueAsBytes(new ErrorResponse(code, errorMessage)));
            } catch (JsonProcessingException e) {
                log.warn(e.getMessage());
                return bufferFactory.wrap(new byte[0]);
            }
        }));
    }
}
