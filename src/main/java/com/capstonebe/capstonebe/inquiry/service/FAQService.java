package com.capstonebe.capstonebe.inquiry.service;

import com.capstonebe.capstonebe.global.exception.CustomErrorCode;
import com.capstonebe.capstonebe.global.exception.CustomException;
import com.capstonebe.capstonebe.inquiry.dto.request.CreateFAQRequest;
import com.capstonebe.capstonebe.inquiry.dto.request.UpdateFAQRequest;
import com.capstonebe.capstonebe.inquiry.dto.response.FAQResponse;
import com.capstonebe.capstonebe.inquiry.entity.FAQ;
import com.capstonebe.capstonebe.inquiry.repository.FAQRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FAQService {

    private final FAQRepository faqRepository;

    @Transactional
    public FAQResponse createFAQ(CreateFAQRequest createRequest) {
        FAQ faq = FAQ.builder()
                .question(createRequest.getQuestion())
                .answer(createRequest.getAnswer())
                .type(createRequest.getType())
                .build();

        faqRepository.save(faq);
        return FAQResponse.from(faq);
    }

    @Transactional
    public FAQResponse updateFAQ(Long id, UpdateFAQRequest updateRequest) {
        FAQ faq = faqRepository.findById(id).orElseThrow(() -> new CustomException(CustomErrorCode.FAQ_NOT_FOUND));

        if (updateRequest.getQuestion() != null) {
            faq.updateQuestion(updateRequest.getQuestion());
        }

        if (updateRequest.getAnswer() != null) {
            faq.updateAnswer(updateRequest.getAnswer());
        }

        if (updateRequest.getType() != null) {
            faq.updateType(updateRequest.getType());
        }

        return FAQResponse.from(faq);
    }

    @Transactional
    public void deleteFAQ(Long id) {
        if (!faqRepository.existsById(id)) {
            throw new CustomException(CustomErrorCode.FAQ_NOT_FOUND);
        }

        faqRepository.deleteById(id);
    }
}
