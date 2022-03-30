package com.example.userservice.security;

import com.example.userservice.dto.UserDTO;
import com.example.userservice.service.IUserService;
import com.example.userservice.service.impl.UserService;
import com.example.userservice.vo.RequestLogin;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

@Slf4j
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    // AuthenticationFilter -> UsernamePasswordAuthenticationToken -> UserDetailService -> AuthenticationFilter
    //                                 토큰으로 변경                      User 로 변경 및 사용      토큰 발행 (User Email 을 가지고 Id값 조회)

    private UserService userService;
    private Environment env;
    @Autowired
    public AuthenticationFilter(Environment env, AuthenticationManager authenticationManager, UserService userService) {
        super.setAuthenticationManager(authenticationManager);
        this.env = env;
        this.userService = userService;
    }


    @Override // ip:port/login
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try {
            // request 한 login 정 (email, pwd)
            RequestLogin creds = new ObjectMapper().readValue(request.getInputStream(), RequestLogin.class);
            return getAuthenticationManager().authenticate(
                    // Token 으로 변환 후 Manager 에 return
                    new UsernamePasswordAuthenticationToken(creds.getEmail(), creds.getPassword(), new ArrayList<>()
                    )
            );

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override // 토큰 만료, 반환값 설정 / 토큰 생성
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,FilterChain chain, Authentication authResult)
            throws IOException, ServletException {
        String userName = ( ((User) authResult.getPrincipal()).getUsername());
        UserDTO userDetails = userService.getUserDetailsByEmail(userName);

        log.info(String.valueOf(userDetails));
        // token 만들기
        String token = Jwts.builder()
                .setSubject(userDetails.getUserId()) // UserId를 가지고 토큰 생성
                .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(env.getProperty("token.expiration_time")))) // now + 24h
                .signWith(SignatureAlgorithm.HS512, env.getProperty("token.secret")) // 암호화 실행
                .compact();

        response.addHeader("token", token);
        response.addHeader("userId", userDetails.getUserId());

    }
}
