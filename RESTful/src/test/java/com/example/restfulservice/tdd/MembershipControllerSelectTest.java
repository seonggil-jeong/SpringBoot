package com.example.restfulservice.tdd;

import com.google.gson.Gson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static com.example.restfulservice.tdd.MembershipConstants.USER_ID_HEADER;
import static com.sun.javaws.JnlpxArgs.verify;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class MembershipControllerSelectTest {

    private final String url = "/api/v1/users/memberships";
    @InjectMocks
    private MembershipController target;

    @Mock
    private MembershipService membershipService;
    private MockMvc mvc;
    private Gson gson;


    @BeforeEach
    public void init() {
        gson = new Gson();
        mvc = MockMvcBuilders.standaloneSetup(target)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("멤버십 목록 조회 실패 - 사용자 식별 is Null")
    void test1() throws Exception {

        ResultActions resultActions = mvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isBadRequest());

    }

    @Test
    @DisplayName("멤버십 목록 조회 실패 - 멤버십이 존재하지않음")
    void test2() throws Exception {
        Mockito.when(membershipService.getMembershipDetail(2L, "userId"))
                .thenThrow(new MembershipException(MembershipErrorResult.MEMBERSHIP_NOT_FOUND));

        ResultActions resultActions = mvc.perform(get("/api/v1/users/memberships/2")
                .header(USER_ID_HEADER, "userId"));


        resultActions.andDo(print());
        resultActions.andExpect(status().isNotFound());


    }

    @Test
    @DisplayName("맴버십 조회 실패 - 소유자 불일치")
    void test3() throws Exception {
        Mockito.when(membershipService.getMembershipDetail(1L, "notowner"))
                .thenThrow(new MembershipException(MembershipErrorResult.NOT_MEMBERSHIP_OWNER));

        ResultActions resultActions = mvc.perform(get("/api/v1/users/memberships/1")
                .header(USER_ID_HEADER, "notowner"));


        resultActions.andDo(print());
        resultActions.andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("멤버십 목록 조회 성공")
    void test4() throws Exception {

        Mockito.when(membershipService.getMembershipListByUserId(any(String.class)))
                .thenReturn(Arrays.asList(
                        MembershipResponse.builder().build(),
                        MembershipResponse.builder().build()
                ));

        final ResultActions resultActions = mvc.perform(get(url)
                .header(USER_ID_HEADER, "12345"));


        resultActions.andExpect(status().isOk());
//        resultActions.andExpect(jsonPath("$.membershipId").isNotEmpty());

        Mockito.verify(membershipService, times(1))
                .getMembershipListByUserId(any(String.class));

    }
}
