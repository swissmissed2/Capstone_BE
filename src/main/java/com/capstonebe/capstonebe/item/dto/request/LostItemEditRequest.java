package com.capstonebe.capstonebe.item.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class LostItemEditRequest {

    @NotNull
    private Long id;              // 어떤 아이템을 수정할지

    @NotBlank
    private String name;

    @NotNull
    private String description;

    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;

    @NotNull
    private LocalDateTime time;

    @NotNull
    private Long categoryId;

    private List<Long> placeIds;
}
