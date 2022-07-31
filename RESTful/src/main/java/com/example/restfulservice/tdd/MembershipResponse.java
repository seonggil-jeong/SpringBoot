package com.example.restfulservice.tdd;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class MembershipResponse {
    private final Long membershipId;
    private final MembershipType membershipType;
}
