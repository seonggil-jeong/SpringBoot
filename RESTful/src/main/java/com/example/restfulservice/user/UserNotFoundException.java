package com.example.restfulservice.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/*
 *  HTTP Status code
 *  2__ -> OK
 *  4__ -> Client Error
 *  5__ -> Server Error
 */

@ResponseStatus(HttpStatus.NOT_FOUND) // 이 예외는 Not_Found 로 오류 리턴
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
