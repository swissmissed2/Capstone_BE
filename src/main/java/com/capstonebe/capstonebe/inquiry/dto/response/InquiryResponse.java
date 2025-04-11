package com.capstonebe.capstonebe.inquiry.dto.response;

import com.capstonebe.capstonebe.inquiry.entity.Inquiry;
import com.capstonebe.capstonebe.inquiry.entity.QuestionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class InquiryResponse {
    private final long id;
    private final String title;
    private final String content;
    private final String answer;

    public static InquiryResponse from(Inquiry inquiry) {
        return new InquiryResponse(
                inquiry.getId(),
                inquiry.getTitle(),
                inquiry.getContent(),
                inquiry.getAnswer()
        );
    }
}
