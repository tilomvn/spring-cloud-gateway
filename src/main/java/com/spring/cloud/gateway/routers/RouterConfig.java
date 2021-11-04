package com.spring.cloud.gateway.routers;

import com.spring.cloud.gateway.filters.AuthorizationFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class RouterConfig {

    @Bean
    RouteLocator gateway(RouteLocatorBuilder rlb, AuthorizationFilter authorizationFilter, ServicesConfigurationInfo services) {
        return new ApiRouterLocator(rlb, services, authorizationFilter);
    }
}
