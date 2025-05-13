package com.capstonebe.capstonebe.item.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
public class AiMatchingResponse {

    @NotNull @JsonProperty("item_id")
    private Long itemId;

    @NotEmpty @JsonProperty("matched_items")
    private List<Map<String, String>> matchedItems;

}
