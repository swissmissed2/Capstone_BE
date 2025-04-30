package com.capstonebe.capstonebe.keword.controller;

import com.capstonebe.capstonebe.keword.dto.request.KeywordRegisterRequest;
import com.capstonebe.capstonebe.keword.service.KeywordService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/keywords")
public class KeywordController {

    private final KeywordService keywordService;

    @PostMapping("/register")
    public ResponseEntity<?> registerKeyword(@RequestBody @Valid KeywordRegisterRequest request,
                                             @AuthenticationPrincipal User user) {

        return ResponseEntity.ok(keywordService.registerKeyword(request, user.getUsername()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteKeyword(@PathVariable("id") Long id,
                                           @AuthenticationPrincipal User user) {

        keywordService.deleteKeyword(id, user.getUsername());
        return ResponseEntity.ok().build();
    }

}
