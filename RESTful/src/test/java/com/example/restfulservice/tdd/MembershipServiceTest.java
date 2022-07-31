package com.example.restfulservice.tdd;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @Mock: Mock 객체를 만들어 반환해주는 어노테이션
 * @Spy: Stub하지 않은 메소드들은 원본 메소드 그대로 사용하는 어노테이션
 * @InjectMocks: @Mock 또는 @Spy로 생성된 가짜 객체를 자동으로 주입시켜주는 어노테이션
 */
@ExtendWith(MockitoExtension.class)
public class MembershipServiceTest {
    private final String USER_ID = "userId";
    private final MembershipType MEMBERSHIP_TYPE = MembershipType.NAVER;
    private final Integer POINT = 10000;

    @InjectMocks
    private MembershipService target; // Test 할 대상

    @Mock
    private MembershipRepository membershipRepository; // Mock 객체를 만들어서 사용


    @Test
    @DisplayName("멤버십등록실패 - 이미 존재하는 멤버십")
    void test1() {

        // findByUserIdAndMembershipType 가 호출될 경우 then Return Membership
        Mockito.when(membershipRepository.findByUserIdAndMembershipType(USER_ID, MEMBERSHIP_TYPE))
                .thenReturn(Membership.builder().build()); // find 를 했을 경우 결과 값이 널이 아닌 경우

        final MembershipException result = Assertions.assertThrows(MembershipException.class, () -> {
            target.addMembership(USER_ID, MEMBERSHIP_TYPE, POINT);
        });

        assertThat(result.getErrorResult()).isEqualTo(MembershipErrorResult.DUPLICATED_MEMBERSHIP_REGISTER);


    }

    @Test
    @DisplayName("멤버십등록성공")
    void test2() {
        Mockito.when(membershipRepository.findByUserIdAndMembershipType(any(String.class), any(MembershipType.class)))
                .thenReturn(null); // 조회 발생 시 중복된 값이 없음

        Mockito.when(membershipRepository.save(any(Membership.class))) // any : 아무 type 이나 상관없이 실행
                .thenReturn(membership());

        // when
        final MembershipResponse result = target.addMembership(USER_ID, MEMBERSHIP_TYPE, POINT);

        assertThat(result.getMembershipId()).isNotNull();
        assertThat(result.getMembershipType()).isEqualTo(MembershipType.NAVER);

        // verify
        verify(membershipRepository, times(1))
                .save(any(Membership.class));

        verify(membershipRepository, times(1))
                .findByUserIdAndMembershipType(USER_ID, MEMBERSHIP_TYPE);
    }

    private Membership membership() {
        return Membership.builder()
                .membershipId(-1L)
                .userId(USER_ID)
                .point(POINT)
                .membershipType(MembershipType.NAVER)
                .build();
    }

}
