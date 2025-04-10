package com.capstonebe.capstonebe.inquiry.service;

import com.capstonebe.capstonebe.global.exception.CustomErrorCode;
import com.capstonebe.capstonebe.global.exception.CustomException;
import com.capstonebe.capstonebe.inquiry.dto.request.CreateInquiryRequest;
import com.capstonebe.capstonebe.inquiry.dto.request.UpdateAnswerRequest;
import com.capstonebe.capstonebe.inquiry.dto.response.InquiryResponse;
import com.capstonebe.capstonebe.inquiry.entity.Inquiry;
import com.capstonebe.capstonebe.inquiry.repository.InquiryRepository;
import com.capstonebe.capstonebe.user.entity.User;
import com.capstonebe.capstonebe.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InquiryService {

    private final InquiryRepository inquiryRepository;
    private final UserRepository userRepository;

    @Transactional
    public InquiryResponse createInquiry(String email, CreateInquiryRequest createRequest) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new CustomException(CustomErrorCode.USER_NOT_FOUND));

        Inquiry inquiry = Inquiry.builder()
                .user(user)
                .title(createRequest.getTitle())
                .content(createRequest.getContent())
                .build();

        inquiryRepository.save(inquiry);
        return InquiryResponse.from(inquiry);
    }

    @Transactional
    public InquiryResponse updateAnswer(Long id, UpdateAnswerRequest updateRequest) {
        Inquiry inquiry = inquiryRepository.findById(id).orElseThrow(() -> new CustomException(CustomErrorCode.INQUIRY_NOT_FOUND));

        inquiry.updateAnswer(updateRequest.getAnswer());

        return InquiryResponse.from(inquiry);
    }

    @Transactional
    public void deleteInquiry(Long id) {
        if(!inquiryRepository.existsById(id)) {
            throw new CustomException(CustomErrorCode.INQUIRY_NOT_FOUND);
        }

        inquiryRepository.deleteById(id);
    }
}
