package com.capstonebe.capstonebe.category.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CategoryEditRequest {

    @NotNull
    private Long id;

    @NotBlank
    private String name;

}
