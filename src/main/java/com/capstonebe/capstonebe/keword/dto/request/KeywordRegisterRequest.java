package com.capstonebe.capstonebe.keword.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class KeywordRegisterRequest {

    @NotBlank
    private String keyword;

}
