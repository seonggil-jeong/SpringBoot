package com.example.apigatewayservice.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderConfig {

    @Bean
    public RouteLocator orderGatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r.path("/order-service/**")  // r.path 가 ~ 일 경우
                        .filters(f -> f.addRequestHeader("order-request", "order-request-header")// add Header
                                .addResponseHeader("order-response", "order-response-header"))
                        .uri("lb://order-service/")).build();
    }
}
