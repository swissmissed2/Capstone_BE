package com.capstonebe.capstonebe.item.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ItemVerifyRequest {

    @NotBlank
    private String identifier;

}
