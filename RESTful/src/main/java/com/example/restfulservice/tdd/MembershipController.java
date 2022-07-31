package com.example.restfulservice.tdd;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;

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

    @GetMapping("/api/v1/users/memberships")
    public ResponseEntity<List<MembershipResponse>> getMembershipList(
            @RequestHeader(USER_ID_HEADER) final String userId
    ) {
        return ResponseEntity.ok().body(membershipService.getMembershipListByUserId(userId));
    }

    @GetMapping("/api/v1/users/memberships/{membershipId}")
    public ResponseEntity<Membership> getMembershipDetail(
            @RequestHeader(USER_ID_HEADER) final String userId,
            @PathVariable Long membershipId
    ) {
        log.info("membershipId : " + membershipId);
        return ResponseEntity.ok(membershipService.getMembershipDetail(membershipId, userId));
    }
}
