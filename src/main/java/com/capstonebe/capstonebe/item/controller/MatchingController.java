package com.capstonebe.capstonebe.item.controller;

import com.capstonebe.capstonebe.item.dto.response.AiMatchingResponse;
import com.capstonebe.capstonebe.item.service.MatchingService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/matching")
@AllArgsConstructor
public class MatchingController {

    private final MatchingService matchingService;

    // ai에서 받은 매칭 내역으로 유저에게 알림 전송
    @PostMapping
    public ResponseEntity<?> sendNotifyByMatching(@RequestBody @Valid AiMatchingResponse response) {

        matchingService.sendMatchingNotify(response);
        return ResponseEntity.ok().build();
    }


}
