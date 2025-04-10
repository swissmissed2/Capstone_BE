package com.capstonebe.capstonebe.inquiry.dto.request;

import com.capstonebe.capstonebe.inquiry.entity.QuestionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateFAQRequest {
    private String question;
    private String answer;
    private QuestionType type;
}