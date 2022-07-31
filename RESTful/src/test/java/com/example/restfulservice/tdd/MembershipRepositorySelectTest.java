package com.example.restfulservice.tdd;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.autoconfigure.filter.TypeExcludeFilters;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTypeExcludeFilter;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

//@TypeExcludeFilters({DataJpaTypeExcludeFilter.class})
//@Transactional
//@AutoConfigureCache
//@AutoConfigureDataJpa
//@AutoConfigureTestDatabase
//@AutoConfigureTestEntityManager
//@ImportAutoConfiguration

@DataJpaTest // Repository Test 시 적용
public class MembershipRepositorySelectTest {

    @Autowired
    MembershipRepository membershipRepository;


    /**
     * findAllBy User Id
     */

    @Test
    @DisplayName("size = 0 - 값이 DB에 없기 때문에 0")
    void test1() {


        List<Membership> membership = membershipRepository.findAllByUserId("userId");

        assertThat(membership.size()).isEqualTo(0);

    }

    @Test
    @DisplayName("전체 조회 - 성공 size = 2")
    void test2() { // 정보가 없을 경우 []를 return 하기 때문에 실패할 수 없음

        // demo
        final Membership naverMembership = Membership.builder()
                .userId("userId")
                .membershipType(MembershipType.NAVER)
                .point(10000)
                .build();

        final Membership kakaoMembership = Membership.builder()
                .userId("userId")
                .membershipType(MembershipType.KAKAO)
                .point(10000)
                .build();

        final Membership kakaoMembershipOther = Membership.builder() // 다른 사용자의 Membership 이기 때문에 조회되면 안됨
                .userId("otherUserId")
                .membershipType(MembershipType.KAKAO)
                .point(10000)
                .build();

        membershipRepository.save(naverMembership);
        membershipRepository.save(kakaoMembership);
        membershipRepository.save(kakaoMembershipOther);


        final List<Membership> membershipList = membershipRepository.findAllByUserId("userId");


        // 값 2개를 넣어뒀기 때문
        assertThat(membershipList.size()).isEqualTo(2);

    }
}
