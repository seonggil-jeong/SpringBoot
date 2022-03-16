package com.example.restfulservice.user;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
// @JsonIgnoreProperties(value = {"password", "ssn"}) // 해당하는 값 무시
@JsonFilter("UserInfo")
public class User {

    private Integer id;

    @Size(min=2, message = "Name은 2글자 이상 입력해 주세요") // 최소 길이 / Bad Request 발생 시 Error message 출력
    private String name;
    @Past // 과거 값만 가능
    private Date join_date;

    // Filtering 을 진행 후 전송해야하는 값들

    // @JsonIgnore / return 무시
    private String password;

    private String ssn;
}
