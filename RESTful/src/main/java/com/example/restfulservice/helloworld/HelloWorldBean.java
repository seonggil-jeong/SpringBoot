package com.example.restfulservice.helloworld;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // Setter, Getter
@AllArgsConstructor // 생성자 생성 ( HelloWorldBean(String) )
@NoArgsConstructor // Default 생성자 생성 ( HelloWorldBean() )
public class HelloWorldBean {
    private String message;

//    public HelloWorldBean(String message) { this.message = message; } / 동시에 실행 시 중복 오류 발생

}
