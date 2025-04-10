package com.capstonebe.capstonebe.inquiry.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateInquiryRequest {
    @NotBlank
    private String title;

    @NotBlank
    private String content;
}
