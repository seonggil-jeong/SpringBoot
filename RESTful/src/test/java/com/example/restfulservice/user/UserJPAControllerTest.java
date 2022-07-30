package com.example.restfulservice.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserJPAController.class)
//@AutoConfigureWebMvc // 이 어노테이션을 통해 MockMvc를 Builder 없이 주입받을 수 있음
class UserJPAControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean // DI 되어 있는 UserRepository 를 Mock 로 구현
    UserRepository userRepository;


    @DisplayName("사용자 조회 test")
    @Test
    void retrieveOneUsers() throws Exception {
        // given : Mock 객체가 특정 상황에서 해야하는 행위를 정의하는 메소드
        given(userRepository.findById(12345)).willReturn(
                Optional.of(new User(12345, "name", new Date(), "password", "ssn"))
        ); // findAll 이 실행된다면 ArrayList 를 return

        mvc.perform(get("/jpa/users/12345"))
                // andExpect : 기대하는 값이 나왔는지 체크해볼 수 있는 메소드
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").exists()) // json path의 depth가 깊어지면 .을 추가하여 탐색할 수 있음 (ex : $.productId.productIdName)
                .andExpect(jsonPath("$.id").exists())
                .andDo(print());


        verify(userRepository).findById(12345); // 해당 객체의 메소드가 실행되었는지 확인

    }


}