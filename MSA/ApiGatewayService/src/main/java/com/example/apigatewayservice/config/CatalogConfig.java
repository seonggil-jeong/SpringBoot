package com.example.apigatewayservice.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CatalogConfig {

    @Bean
    public RouteLocator userGatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r.path("/catalog-service/**")  // r.path 가 ~ 일 경우
                        .filters(f -> f.addRequestHeader("catalog-request", "catalog-request-header") // add Header
                                .addResponseHeader("catalog-response", "catalog-response-header"))
                        .uri("lb://catalog-service/")).build();
    }
}
