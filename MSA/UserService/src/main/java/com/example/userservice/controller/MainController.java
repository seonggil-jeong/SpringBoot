package com.example.userservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class MainController {

    private final Environment env;

    @Autowired
    public MainController(Environment env) {
        this.env = env;
    }

    @GetMapping("/health")
    public String status() {
        return "(1)" + String.format("%s (%s)", env.getProperty("spring.application.name"), env.getProperty("local.server.port")); // spring.application.name in yml
    }
}
