package com.capstonebe.capstonebe.item.controller;

import com.capstonebe.capstonebe.item.dto.request.MatchingRequest;
import com.capstonebe.capstonebe.item.service.MatchingService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/matching")
@AllArgsConstructor
public class MatchingController {

    private final MatchingService matchingService;

    // todo : ai에서 받은 매칭 내역으로 유저에게 알림 전송
    @GetMapping
    public ResponseEntity<?> sendNotifyByMatching(@RequestBody MatchingRequest request) {

        return ResponseEntity.ok().build();
    }


    // 매칭 알림 전송 테스트
    @GetMapping("/test3")
    public ResponseEntity<?> sendMatchingNotify(@RequestBody @Valid MatchingRequest request) {

        matchingService.sendMatchingNotify(request);

        return ResponseEntity.ok().build();
    }

}
