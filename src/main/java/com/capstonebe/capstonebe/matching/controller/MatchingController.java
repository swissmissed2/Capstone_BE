package com.capstonebe.capstonebe.matching.controller;

import com.capstonebe.capstonebe.matching.service.MatchingService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

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


}
