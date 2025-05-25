package com.capstonebe.capstonebe.item.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;


@Getter
@Setter
public class LostItemRegisterRequest {

    @NotBlank
    private String name;

    //@NotNull
    private LocalDate time;

    @NotBlank
    private String description;

    @NotNull
    private Long categoryId;

    private List<Long> placeIds;

}
