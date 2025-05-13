package com.capstonebe.capstonebe.inquiry.controller;

import com.capstonebe.capstonebe.global.entity.BaseEntity;
import com.capstonebe.capstonebe.inquiry.dto.request.CreateInquiryRequest;
import com.capstonebe.capstonebe.inquiry.dto.request.UpdateAnswerRequest;
import com.capstonebe.capstonebe.inquiry.dto.response.InquiryResponse;
import com.capstonebe.capstonebe.inquiry.service.InquiryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/inquiries")
@RequiredArgsConstructor
public class InquiryController extends BaseEntity {

    private final InquiryService inquiryService;

    // 문의 생성
    @PostMapping
    public ResponseEntity<InquiryResponse> createInquiry(@AuthenticationPrincipal UserDetails userDetails, @RequestBody @Valid CreateInquiryRequest createRequest) {
        InquiryResponse response = inquiryService.createInquiry(userDetails.getUsername(), createRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 답변 추가
    @PatchMapping("/{id}")
    public ResponseEntity<InquiryResponse> updateAnswer(@PathVariable Long id, @RequestBody @Valid UpdateAnswerRequest updateRequest) {
        InquiryResponse response = inquiryService.updateAnswer(id, updateRequest);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 문의 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInquiry(@PathVariable Long id) {
        inquiryService.deleteInquiry(id);
        return ResponseEntity.noContent().build();
    }

    // 문의 전체 조회
    @GetMapping
    public ResponseEntity<Page<InquiryResponse>> getAllInquiries(Pageable pageable) {
        Page<InquiryResponse> inquiries = inquiryService.getAllInquiries(pageable);
        return ResponseEntity.ok(inquiries);
    }
}
