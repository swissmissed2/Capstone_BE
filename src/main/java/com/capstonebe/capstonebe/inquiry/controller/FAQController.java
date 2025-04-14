package com.capstonebe.capstonebe.inquiry.controller;

import com.capstonebe.capstonebe.inquiry.dto.request.CreateFAQRequest;
import com.capstonebe.capstonebe.inquiry.dto.request.UpdateFAQRequest;
import com.capstonebe.capstonebe.inquiry.dto.response.FAQResponse;
import com.capstonebe.capstonebe.inquiry.entity.QuestionType;
import com.capstonebe.capstonebe.inquiry.service.FAQService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/faqs")
@RequiredArgsConstructor
public class FAQController {

    private final FAQService faqService;

    // FAQ 생성
    @PostMapping
    public ResponseEntity<FAQResponse> createFAQ(@RequestBody @Valid CreateFAQRequest createRequest) {
        FAQResponse response = faqService.createFAQ(createRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    //FAQ 수정
    @PatchMapping("/update/{id}")
    public ResponseEntity<FAQResponse> updatePost(@PathVariable Long id, @RequestBody UpdateFAQRequest updateRequest) {
        FAQResponse response = faqService.updateFAQ(id, updateRequest);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // FAQ 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFAQ(@PathVariable Long id) {
        faqService.deleteFAQ(id);
        return ResponseEntity.noContent().build();
    }

    // 타입 별 FAQ 조회
    @GetMapping
    public ResponseEntity<Page<FAQResponse>> getFAQsByType(@RequestParam("type") QuestionType questionType, Pageable pageable) {
        Page<FAQResponse> faqs = faqService.getFAQsByType(questionType, pageable);
        return ResponseEntity.ok(faqs);
    }
}
