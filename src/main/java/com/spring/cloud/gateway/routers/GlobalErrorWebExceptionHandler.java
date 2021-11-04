package com.spring.cloud.gateway.routers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.cloud.gateway.common.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.ConnectException;

@Component
class GlobalErrorWebExceptionHandler implements ErrorWebExceptionHandler {

    //TODO move msg to messages.properties
    private final String RemoteServiceFailed = "Remote Service Unavailable";

    Logger log = LoggerFactory.getLogger(GlobalErrorWebExceptionHandler.class);

    final ObjectMapper objectMapper;

    GlobalErrorWebExceptionHandler(ObjectMapper objectMapper) { this.objectMapper = objectMapper; }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        ServerHttpResponse response = exchange.getResponse();
        if (response.isCommitted()) {
            return Mono.error(ex);
        }

        String responseMessage = ex.getMessage();

        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        if (ex instanceof ConnectException) { //connection refused by called to others service
            responseMessage = RemoteServiceFailed;
        }
        //TODO: any other ex should be handled ?

        log.info(ex.getMessage());

        String finalResponseMessage = responseMessage;
        return response.writeWith(Mono.fromSupplier(() -> {
                    DataBufferFactory bufferFactory = response.bufferFactory();
                    try {
                        return bufferFactory.wrap(objectMapper.writeValueAsBytes(new ErrorResponse(500, finalResponseMessage)));
                    } catch (JsonProcessingException e) {
                        log.warn(e.getMessage());
                        return bufferFactory.wrap(new byte[0]);
                    }
                }));
    }
}