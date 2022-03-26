package com.example.userservice.security;

import com.example.userservice.service.IUserService;
import com.example.userservice.service.impl.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.Resource;

@Configuration
@EnableWebSecurity
@Slf4j
public class WebSecurity extends WebSecurityConfigurerAdapter {
    private UserService userService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private Environment env;

    public WebSecurity(BCryptPasswordEncoder bCryptPasswordEncoder, Environment env, UserService userService) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.env = env; // 토큰 유효 시간 ~~ 을 yml 에서 관리
        this.userService = userService;
    }


    @Override // 권한 작업
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
//        http.authorizeRequests().antMatchers("/users/**").permitAll(); // 모든 요청 통과

        // 인증이 되어진 상태에서만 통과 진행 (통과 조건)
        http.authorizeRequests().antMatchers("/**")
                .hasIpAddress("192.168.45.152") // 특정 IP 만 가능 ( 127 ~~ 접근 X )
                .and()
                .addFilter(getAuthenticationFilter()); // 선언한 필터 적용

        http.headers().frameOptions().disable();
    }

    private AuthenticationFilter getAuthenticationFilter() throws Exception{
        AuthenticationFilter authenticationFilter = new AuthenticationFilter();
        authenticationFilter.setAuthenticationManager(authenticationManager());

        return authenticationFilter;
    }

    // select pwd from users where ~
    // db <-> input 비교    / input 값을 enc 진행
    @Override // 인증 작업
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder);
    }
}
