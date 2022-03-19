package com.example.apigatewayservice.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserConfig {

    @Bean
    public RouteLocator userGatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r.path("/user-service/**")  // r.path 가 ~ 일 경우
                        .filters(f -> f.addRequestHeader("user-request", "user-request-header") // add Header
                                .addResponseHeader("user-response", "user-response-header"))
                        .uri("lb://user-service/")).build();
    }
}
