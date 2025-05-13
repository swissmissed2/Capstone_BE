package com.capstonebe.capstonebe.matching.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateMatchingRequest {
    @NotNull
    private Long itemId;

    @NotNull
    private List<Long> matchedItemIds;
}
