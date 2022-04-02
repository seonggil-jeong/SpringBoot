package com.example.userservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class MainController {

    private final Environment env;

    @Autowired
    public MainController(Environment env) {
        this.env = env;
    }

    @GetMapping("/health-check")
    public String status() {

        return env.getProperty("spring.application.name")
                + ", port(local.server.port) : " + env.getProperty("local.server.port")
                + ", port(server.port) : " + env.getProperty("server.port")
                + ", token secret : " + env.getProperty("token.secret")
                + ", token expiration time : " + env.getProperty("token.expiration_time");

    }
}
