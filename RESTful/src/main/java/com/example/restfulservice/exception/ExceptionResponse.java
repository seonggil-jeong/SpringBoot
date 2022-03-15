package com.example.restfulservice.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

// Exception Handling
@Data // Getter, Setter
@AllArgsConstructor // 모든 필드를 가지고 있는 생성자
@NoArgsConstructor // Default 생성자
public class ExceptionResponse {
    private Date timestamp; // 발생 시간
    private String message; // 내용
    private String details; // 상세


}
