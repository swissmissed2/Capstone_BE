package com.capstonebe.capstonebe.image.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

@Getter
public class ImageRegisterRequest {

    @NotNull
    private Long itemId;

    @NotEmpty
    private List<String> paths;
}
