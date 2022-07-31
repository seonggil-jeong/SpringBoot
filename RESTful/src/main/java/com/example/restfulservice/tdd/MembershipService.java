package com.example.restfulservice.tdd;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public List<MembershipResponse> getMembershipListByUserId(String userId) {
        List<Membership> membershipList = membershipRepository.findAllByUserId(userId);

        return membershipList.stream()
                .map(membership -> MembershipResponse.builder()
                        .membershipId(membership.getMembershipId())
                        .membershipType(membership.getMembershipType())
                        .build())
                .collect(Collectors.toList());
    }

    public Membership getMembershipDetail(long membershipId, String userId) {

        final Optional<Membership> membershipOptional = membershipRepository.findById(membershipId);

        final Membership membership = membershipOptional.orElseThrow(() -> new MembershipException(MembershipErrorResult.MEMBERSHIP_NOT_FOUND));

        if (!membership.getUserId().equals(userId)) {
            throw new MembershipException(MembershipErrorResult.NOT_MEMBERSHIP_OWNER);
        }

        return membership;
    }
}
