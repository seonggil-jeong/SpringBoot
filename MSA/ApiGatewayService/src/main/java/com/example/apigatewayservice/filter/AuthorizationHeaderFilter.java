package com.example.apigatewayservice.filter;


import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component // filter
@Slf4j
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {
    Environment env;

    public AuthorizationHeaderFilter(Environment env) {
        super(Config.class);
        this.env = env;
    }
    public static class Config {

    }
    // login -> get token -> /users (with token) -> token open and check
    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();  // 사용자가 보낸 요청 (token)
            if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) { // 인증과 관련된 값이 있는가?
                // False
                return onError(exchange, "no authorization Header", HttpStatus.UNAUTHORIZED);
            }
            // 받아온 토큰 정보가 들어 있음
            String authorizationHeader = request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            String jwt = authorizationHeader.replace("Bearer", ""); // Bearer token 확인

            if (!isJwtValid(jwt)) { // 토큰 값이 맞는가? 정상 값인가
                return onError(exchange, "JWT token is not valid", HttpStatus.UNAUTHORIZED);
            }

            return chain.filter(exchange);
        });
    }
    private boolean isJwtValid(String jwt) {
        boolean returnValue = true;

        String subject = null;
        try {
            subject = Jwts.parser().setSigningKey(env.getProperty("token.secret")) // 복호화
                    .parseClaimsJws(jwt).getBody().getSubject(); // sub 값 추출

        } catch (Exception e) {
            returnValue = false;
        }

        if (subject == null || subject.isEmpty()) {
            returnValue = false;
        }


        return returnValue;

    }

    // Mono, Flux -> Spring WebFlux (단일 값 = Mono)
    private Mono<Void> onError(ServerWebExchange exchange, String error, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        log.error(error);

        return response.setComplete();
    }
}
