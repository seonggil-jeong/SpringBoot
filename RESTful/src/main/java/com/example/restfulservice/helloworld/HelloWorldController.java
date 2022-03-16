package com.example.restfulservice.helloworld;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

@Slf4j
@RestController // 자동으로 json 형태로 return
public class HelloWorldController {

    @GetMapping(path = "/hello-world")
    public String helloWorld() {
        return "Hello World";
    }

    // alt + enter 로 Class 생성
    @GetMapping(path = "/hello-world-bean") // bean 형태로 반환
    public HelloWorldBean helloWorldBean() {

        return new HelloWorldBean("Hello World");
    }

    // uri 에 가변 변수 사용하기 ( Path Variable )
    @GetMapping(path = "/hello-world-bean/path-variable/{name}") // {} 값을 가변적으로 지정 / @PathVariable
    public HelloWorldBean helloWorldBean(@PathVariable String name) {
        return new HelloWorldBean(String.format("Hello World, %s", name));
    }
}
