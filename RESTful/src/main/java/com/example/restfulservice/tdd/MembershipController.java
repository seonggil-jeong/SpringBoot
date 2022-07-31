package com.example.restfulservice.tdd;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.example.restfulservice.tdd.MembershipConstants.USER_ID_HEADER;

@RestController
@Slf4j
@RequiredArgsConstructor
public class MembershipController {
    private final MembershipService membershipService;

    @PostMapping("/api/v1/users/memberships")
    public ResponseEntity<MembershipResponse> addMembership(
            @RequestHeader(USER_ID_HEADER) final String userId,
            @RequestBody @Valid final MembershipRequest membershipRequest) {

        membershipService.addMembership(userId, membershipRequest.getMembershipType(), membershipRequest.getPoint());


        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
