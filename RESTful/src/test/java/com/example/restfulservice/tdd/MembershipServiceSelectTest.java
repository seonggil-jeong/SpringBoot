package com.example.restfulservice.tdd;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class MembershipServiceSelectTest {
    private final String userId = "userId";

    @Mock
    private MembershipRepository membershipRepository;

    @InjectMocks
    private MembershipService membershipService;


    @Test
    @DisplayName("조회 성공 (size = 2) - 항상 성공")
    void test1() {
        Mockito.when(membershipRepository.findAllByUserId(any(String.class)))
                .thenReturn(Arrays.asList(
                        Membership.builder().build(),
                        Membership.builder().build()
                ));

        List<MembershipResponse> membershipResponses = membershipService.getMembershipListByUserId(userId);

        assertThat(membershipResponses.size()).isEqualTo(2);
    }

    ////////// 단일 조회

    @Test
    @DisplayName("멤버십상세조회실패 - 존재하지 않는 멤버십아이디")
    void test2() {
        Mockito.when(membershipRepository.findById(1L))
                .thenReturn(Optional.empty()); // 빈 값을 return

        final MembershipException result = assertThrows(MembershipException.class, () -> membershipService.getMembershipDetail(1L, userId));

        assertThat(result.getErrorResult()).isEqualTo(MembershipErrorResult.MEMBERSHIP_NOT_FOUND);

    }

    @Test
    @DisplayName("맴버십상세조회실패 - 존재하는 멤버십이지만, 사용자 불일치")
    void test3() {
        Mockito.when(membershipRepository.findById(1L))
                .thenReturn(Optional.ofNullable(membership()));

        final MembershipException result = assertThrows(MembershipException.class, () -> membershipService.getMembershipDetail(1L, "notowner"));

        assertThat(result.getErrorResult()).isEqualTo(MembershipErrorResult.NOT_MEMBERSHIP_OWNER);
    }


    @Test
    @DisplayName("멤버십상세조회성공")
    void test4() {
        Mockito.when(membershipRepository.findById(1L))
                .thenReturn(Optional.of(membership()));

        Membership membership = membershipService.getMembershipDetail(1L, userId);
        assertThat(membership.getMembershipType()).isEqualTo(MembershipType.NAVER);
        assertThat(membership.getPoint()).isEqualTo(10000);
    }


    private Membership membership() {
        return Membership.builder()
                .membershipId(1L)
                .userId(userId)
                .point(10000)
                .membershipType(MembershipType.NAVER)
                .build();
    }
}
