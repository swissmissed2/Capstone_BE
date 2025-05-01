package com.capstonebe.capstonebe.item.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

@Getter
public class MatchingRequest {

    @NotNull
    private Long foundItemId;

    @NotEmpty
    private List<Long> lostItemId;

}
