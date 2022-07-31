package com.example.restfulservice.tdd;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MembershipService {
    private final MembershipRepository membershipRepository;

    public MembershipResponse addMembership(final String userId, final MembershipType membershipType, final Integer point) {

        final Membership result = membershipRepository.findByUserIdAndMembershipType(userId, membershipType);

        if (result != null) { // Null 이 아니라면 Exception
            throw new MembershipException(MembershipErrorResult.DUPLICATED_MEMBERSHIP_REGISTER);
        }
        Membership membership = Membership.builder()
                .userId(userId)
                .membershipType(membershipType)
                .point(point)
                .build();

        Membership saveMembership = membershipRepository.save(membership);


        return MembershipResponse.builder()
                .membershipId(saveMembership.getMembershipId())
                .membershipType(saveMembership.getMembershipType())
                .build();

    }
}
