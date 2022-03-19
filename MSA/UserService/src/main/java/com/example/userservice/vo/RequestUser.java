package com.example.userservice.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestUser { // rest-api 요청 시 받을 값   ( Request -> DTO -> Entity)

    @NotNull(message = "Email cannot noll")
    @Size(min = 2, message = "Email not be less than two char")
    @Email
    private String email;

    @NotNull(message = "Password cannot be null")
    @Size(min = 8, message = "Password must be equal or grater than 8 char and less than 16 char")
    private String pwd;

    @NotNull(message = "Name cannot null")
    @Size(min = 2, message = "Name not be less than two char")
    private String name;
}
