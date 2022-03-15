package com.example.restfulservice.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private Integer id;

    @Size(min=2, message = "Name은 2글자 이상 입력해 주세요") // 최소 길이 / Bad Request 발생 시 Error message 출력
    private String name;
    @Past // 과거 값만 가능
    private Date join_date;
}
