package com.spring.cloud.gateway.filters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.cloud.gateway.common.AuthenticationService;
import com.spring.cloud.gateway.common.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class AuthorizationFilter implements GatewayFilter {

    Logger log = LoggerFactory.getLogger(AuthorizationFilter.class);

    //TODO : move msg to messages.properties
    private final String unAuthorizedMessage = "Un-authorized";

    final ObjectMapper objectMapper;
    final AuthenticationService authenticationService;

    AuthorizationFilter(ObjectMapper objectMapper, AuthenticationService authenticationService) {
        this.objectMapper = objectMapper;
        this.authenticationService = authenticationService;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        List<String> headers = exchange.getRequest().getHeaders().getOrEmpty("Authorization");
        if (headers.size() == 0) {
            return onError(exchange, HttpStatus.FORBIDDEN.value(), unAuthorizedMessage);
        } else {
            String jwtToken = headers.get(0);
            AuthenticationService.AuthorizationResponse authorizationResponse = authenticationService.authorize(jwtToken);
            if (!authorizationResponse.getAuthResult()) {
                return onError(exchange, HttpStatus.FORBIDDEN.value(), authorizationResponse.getMessage());
            }
            return chain.filter(exchange);
        }
    }

    Mono<Void> onError(ServerWebExchange exchange, int code, String errorMessage) {
        ServerHttpResponse response = exchange.getResponse();
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        response.setStatusCode(HttpStatus.FORBIDDEN);
        return response.writeWith(Mono.fromSupplier(() -> {
            DataBufferFactory bufferFactory = response.bufferFactory();
            try {
                return bufferFactory.wrap(objectMapper.writeValueAsBytes(new ErrorResponse(code, errorMessage)));
            } catch (JsonProcessingException e) {
                log.warn(e.getMessage());
                return bufferFactory.wrap(new byte[0]);
            }
        }));
    }
}
