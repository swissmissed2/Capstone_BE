package com.capstonebe.capstonebe.matching.controller;

import com.capstonebe.capstonebe.matching.dto.request.CreateMatchingRequest;
import com.capstonebe.capstonebe.matching.dto.response.MatchingResponse;
import com.capstonebe.capstonebe.matching.service.MatchingService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/matching")
@AllArgsConstructor
public class MatchingController {

    private final MatchingService matchingService;

//    // ai에서 받은 매칭 내역으로 유저에게 알림 전송
//    @PostMapping
//    public ResponseEntity<?> sendNotifyByMatching(@RequestBody @Valid AiMatchingResponse response) {
//
//        matchingService.sendMatchingNotify(response);
//        return ResponseEntity.ok().build();
//    }

    @PostMapping
    public ResponseEntity<List<MatchingResponse>> createMatching(@RequestBody @Valid CreateMatchingRequest createRequest) {
        List<MatchingResponse> responses = matchingService.createMatching(createRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(responses);
    }
}
