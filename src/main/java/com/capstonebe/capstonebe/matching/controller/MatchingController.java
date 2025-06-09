package com.capstonebe.capstonebe.matching.controller;

import com.capstonebe.capstonebe.global.exception.CustomErrorCode;
import com.capstonebe.capstonebe.global.exception.CustomException;
import com.capstonebe.capstonebe.item.dto.response.ItemListResponse;
import com.capstonebe.capstonebe.matching.dto.request.ConfirmOwnerRequest;
import com.capstonebe.capstonebe.matching.dto.request.CreateMatchingRequest;
import com.capstonebe.capstonebe.matching.dto.response.MatchingResponse;
import com.capstonebe.capstonebe.matching.service.MatchingService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/matching")
@AllArgsConstructor
public class MatchingController {

    private final MatchingService matchingService;

    @PostMapping
    public ResponseEntity<List<MatchingResponse>> createMatching(@RequestBody @Valid CreateMatchingRequest createRequest) {
        List<MatchingResponse> responses = matchingService.createMatching(createRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Page<ItemListResponse>> getMatchedItemsByUser(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id, Pageable pageable) {
        if (userDetails == null) {
            throw new CustomException(CustomErrorCode.UNAUTHORIZED);
        }

        Page<ItemListResponse> responses = matchingService.getMatchedItemsByUser(userDetails.getUsername(), id, pageable);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/owner")
    public ResponseEntity<Void> confirmOwnership(@RequestBody @Valid ConfirmOwnerRequest confirmRequest) {
        matchingService.confirmOwnership(confirmRequest);
        return ResponseEntity.ok().build();
    }
}
