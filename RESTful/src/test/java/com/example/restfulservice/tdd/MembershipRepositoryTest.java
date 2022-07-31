package com.example.restfulservice.tdd;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
@DataJpaTest // @DataJpaTest: JPA Repository 들에 대한 빈들을 등록하여 단위 테스트의 작성을 용이하게 함
public class MembershipRepositoryTest {

    @Autowired
    private MembershipRepository membershipRepository;

    @Test
    @Order(1)
    @DisplayName("Bean 생성 확인")
    void test1() {
        assertThat(membershipRepository).isNotNull();
    }

    @Test
    @Order(2)
    @DisplayName("멤버십등록 Test")
    void test2() {
        final Membership membership = Membership.builder()
                .userId("userId")
                .membershipType(MembershipType.NAVER)
                .point(10000)
                .build();

        final Membership result = membershipRepository.save(membership);

        assertThat(result.getMembershipId()).isNotNull(); // 자동으로 생성되었는지 확인해야 함

        // request 값과 같아야 함
        assertThat(result.getPoint()).isEqualTo(membership.getPoint());
        assertThat(result.getMembershipType()).isEqualTo(MembershipType.NAVER);
    }

    @Test
    @Order(3)
    @DisplayName("멤버십이 이미 존재하는지 확인 -> 중복 발생")
    void test3() {
        final Membership membership = Membership.builder()
                .userId("userId")
                .membershipType(MembershipType.NAVER)
                .point(10000)
                .build();

        membershipRepository.save(membership); // demo 값을 넣어줌

        // 아이디 및 Type 모두 일치하는 경우 중복 생성 발생
        final Membership result = membershipRepository.findByUserIdAndMembershipType("userId", MembershipType.NAVER);

        assertThat(result).isNotNull(); // 결과가 있어야 함  / 중복
        assertThat(result.getUserId()).isEqualTo(membership.getUserId()); // 아이디 값도 일치해야 함
        assertThat(result.getMembershipType()).isEqualTo(membership.getMembershipType()); // Type 도 일치해야 함


    }
}
