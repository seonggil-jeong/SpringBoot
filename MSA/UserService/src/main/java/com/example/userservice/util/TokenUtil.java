package com.example.userservice.util;

import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TokenUtil {

    // Token 안에서 UserId 가져오기
    public static String getUserIdFromToken(String requestJwt, String secret) { // JWT, token.secret Code
        log.info("getUserId Start!");

        String jwt = requestJwt.replace("Bearer", ""); // Bearer 삭제

        log.info("JWT : " + jwt); // TokenValue

        String userId = Jwts.parser().setSigningKey(secret).parseClaimsJws(jwt).getBody().getSubject(); // 복호화

        log.info("getUserId End");


        return userId;
    }

    // 토큰 가져오기
    public static String getToken(String requestJwt) {
        log.info("getUserId Start!");

        String jwt = requestJwt.replace("Bearer", ""); // Bearer 삭제

        log.info("JWT : " + jwt); // TokenValue

        return jwt;
    }
}
