package com.capstonebe.capstonebe.inquiry.dto.response;

import com.capstonebe.capstonebe.inquiry.entity.FAQ;
import com.capstonebe.capstonebe.inquiry.entity.QuestionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class FAQResponse {
    private final long id;
    private final String question;
    private final String answer;
    private final QuestionType type;

    public static FAQResponse from(FAQ faq) {
        return new FAQResponse(
                faq.getId(),
                faq.getQuestion(),
                faq.getAnswer(),
                faq.getQuestionType()
        );
    }
}
