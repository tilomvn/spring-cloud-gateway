package com.spring.cloud.gateway.routers;

import com.spring.cloud.gateway.filters.AuthorizationFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import reactor.core.publisher.Flux;

class ApiRouterLocator implements RouteLocator {

    final RouteLocatorBuilder routeLocatorBuilder;
    final ServicesConfigurationInfo servicesConfigurationInfo;
    final AuthorizationFilter authorizationFilter;

    ApiRouterLocator(RouteLocatorBuilder routeLocatorBuilder, ServicesConfigurationInfo servicesConfigurationInfo,
                    AuthorizationFilter authorizationFilter) {
        this.routeLocatorBuilder = routeLocatorBuilder;
        this.servicesConfigurationInfo = servicesConfigurationInfo;
        this.authorizationFilter = authorizationFilter;
    }

    @Override
    public Flux<Route> getRoutes() {
        RouteLocatorBuilder.Builder routesBuilder = routeLocatorBuilder.routes();
        return Flux.fromStream(servicesConfigurationInfo.getServices().stream())
                .map(service -> routesBuilder.route(service.getId(), routeSpecs -> routeSpecs.path(service.getEndpoint())
                                .filters(f -> service.getNeedAuthorize() ? f.filter(authorizationFilter) : f)
                                .uri(service.getHost())))
                .collectList()
                .flatMapMany(builders -> routesBuilder.build().getRoutes());
    }
}
