package com.capstonebe.capstonebe.inquiry.dto.request;

import com.capstonebe.capstonebe.inquiry.entity.QuestionType;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateFAQRequest {
    @NotBlank
    private String question;

    @NotBlank
    private String answer;

    @NotBlank
    private QuestionType questionType;
}
