package com.capstonebe.capstonebe.category.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CategoryRegisterRequest {

    @NotBlank
    String name;
}
