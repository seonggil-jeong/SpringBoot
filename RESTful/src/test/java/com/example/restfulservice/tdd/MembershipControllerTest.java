package com.example.restfulservice.tdd;

import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeAll;
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
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static com.example.restfulservice.tdd.MembershipConstants.USER_ID_HEADER;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class MembershipControllerTest {

    @InjectMocks
    private MembershipController target;
    /**
     * 컨트롤러는 함수 호출이 아닌 API 호출을 통해 요청을 받고 응답을 처리해야 하며, 메세지 컨버팅 등과 같은 작업이 필요하다
     * 그러므로 MockMvc 라는 클래스를 이용
     */

    @Mock
    private MembershipService membershipService;

    private MockMvc mvc;
    private Gson gson;

    final String url = "/api/v1/users/memberships";

    @BeforeEach
    void init() {
        gson = new Gson();
        mvc = MockMvcBuilders.standaloneSetup(target)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @DisplayName("Bean 등록 Test - Gson, MockMvc")
    @Test
    void test1() {
        assertThat(mvc).isNotNull();
        assertThat(gson).isNotNull();
    }

    @Test
    @DisplayName("등록 실패 - 식별자를 확인할 수 없음, Header 에 사용자 아이디가 없을 때")
    void test2() throws Exception {

        final ResultActions resultActions = mvc.perform(
                post(url)
                        .content(gson.toJson(membershipRequest(10000, MembershipType.NAVER)))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        resultActions.andExpect(status().isBadRequest()); // BadRequest 를 반환 해야함
        resultActions.andDo(print());

    }

    @Test
    @DisplayName("멤버십 등록 실패 - 포인트가 Null or - ")
    void test3() throws Exception {

        ResultActions resultActions = mvc.perform(post(url) // point 가 음수인 case
                .contentType(MediaType.APPLICATION_JSON)
                .header(USER_ID_HEADER, "12345")
                .content(gson.toJson(membershipRequest(-10, MembershipType.NAVER))));

        ResultActions resultActions2 = mvc.perform(post(url) // point 가 null 인 case
                .contentType(MediaType.APPLICATION_JSON)
                .header(USER_ID_HEADER, "12345")
                .content(gson.toJson(membershipRequest(null, MembershipType.NAVER))));


        resultActions2.andExpect(status().isBadRequest());
        resultActions.andExpect(status().isBadRequest());
    }


    @Test
    @DisplayName("BAD REQUEST - 멤버십 타입이 Null 일 경우")
    void test4() throws Exception {

        ResultActions resultActions = mvc.perform(post(url) // Type 이 Null 인 경우
                .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_ID_HEADER, "12345")
                .content(gson.toJson(membershipRequest(10000, null))));

        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("멤버십등록 실패 - Throw Error in MembershipService")
    void test5() throws Exception {
        // given addMembership 요청했을 경우, Exception -> 중복이 발생한 경우
        Mockito.when(membershipService.addMembership("12345", MembershipType.NAVER, 10000))
                .thenThrow(new MembershipException(MembershipErrorResult.DUPLICATED_MEMBERSHIP_REGISTER));

        final ResultActions resultActions = mvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .header(USER_ID_HEADER, "12345")
                .content(gson.toJson(membershipRequest(10000, MembershipType.NAVER))));

        // then
        resultActions.andExpect(status().isBadRequest());
    }


    private MembershipRequest membershipRequest(final Integer point, final MembershipType membershipType) {
        return MembershipRequest.builder()
                .point(point)
                .membershipType(membershipType)
                .build();
    }

}
