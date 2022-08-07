package com.spring.cloud.gateway.routers;

import com.spring.cloud.gateway.filters.AuthorizationFilter;
import org.springframework.cloud.gateway.filter.factory.RetryGatewayFilterFactory;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Flux;

class ApiRouterLocator implements RouteLocator {

    final RouteLocatorBuilder routeLocatorBuilder;
    final ServicesConfigurationInfo servicesConfigurationInfo;
    final AuthorizationFilter authorizationFilter;
    final RetryGatewayFilterFactory retryGatewayFilterFactory;

    ApiRouterLocator(RouteLocatorBuilder routeLocatorBuilder, ServicesConfigurationInfo servicesConfigurationInfo,
                    AuthorizationFilter authorizationFilter, RetryGatewayFilterFactory retryGatewayFilterFactory) {
        this.routeLocatorBuilder = routeLocatorBuilder;
        this.servicesConfigurationInfo = servicesConfigurationInfo;
        this.authorizationFilter = authorizationFilter;
        this.retryGatewayFilterFactory = retryGatewayFilterFactory;
    }

    @Override
    public Flux<Route> getRoutes() {
        var routesBuilder = routeLocatorBuilder.routes();

        var retryConfig = new RetryGatewayFilterFactory.RetryConfig()
                .setStatuses(HttpStatus.UNAUTHORIZED).setMethods(HttpMethod.GET, HttpMethod.POST).setRetries(1);

        return Flux.fromStream(servicesConfigurationInfo.services.stream())
                .map(service -> {
                    retryConfig.setRouteId(service.id);
                    return routesBuilder.route(service.id, routeSpecs -> routeSpecs.path(service.endpoint)
                                .filters(f -> {
                                    f.rewritePath(service.id, "");//fix bug forward predicates when using with eureka
                                    if (service.needAuthorize) { f.filter(authorizationFilter); }
                                    else { f.filter(retryGatewayFilterFactory.apply(retryConfig)); }
                                    return f;
                                })
                                .uri(service.host));
                })
                .collectList()
                .flatMapMany(builders -> routesBuilder.build().getRoutes());
    }
}
