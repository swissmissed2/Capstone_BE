package com.capstonebe.capstonebe.image.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ImageEditRequest {

    @NotNull
    private Long id;

    @NotNull
    private String field;

}
