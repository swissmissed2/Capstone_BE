package com.capstonebe.capstonebe.item.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FoundItemRegisterRequest {

    @NotBlank
    private String name;

    private Double latitude;

    private Double longitude;

    @NotBlank
    private String description;

    @NotNull
    private Long categoryId;

    private List<Long> placeIds;

}
