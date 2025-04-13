package com.capstonebe.capstonebe.image.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ImageRegisterRequest {

    @NotNull
    private Long itemId;

    @NotBlank
    private String path;

}
