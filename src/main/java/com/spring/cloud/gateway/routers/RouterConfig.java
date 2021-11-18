package com.spring.cloud.gateway.routers;

import com.spring.cloud.gateway.filters.AuthorizationFilter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.gateway.filter.factory.RetryGatewayFilterFactory;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class RouterConfig {

    @Bean
    RestTemplateBuilder restTemplateBuilder() { return new RestTemplateBuilder(); }

    @Bean("myRetryGatewayFilterFactory")
    RetryGatewayFilterFactory retryGatewayFilterFactory(RestTemplateBuilder restTemplateBuilder) {
        return new MyRetryGatewayFilterFactory(restTemplateBuilder);
    }

    @Bean
    RouteLocator gateway(RouteLocatorBuilder rlb, AuthorizationFilter authorizationFilter, ServicesConfigurationInfo services,
                         @Qualifier("myRetryGatewayFilterFactory") RetryGatewayFilterFactory retryGatewayFilterFactory) {
        return new ApiRouterLocator(rlb, services, authorizationFilter, retryGatewayFilterFactory);
    }
}
