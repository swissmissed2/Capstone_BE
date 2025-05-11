package com.capstonebe.capstonebe.item.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class AiMatchingRequest {

    @JsonProperty("item_id")
    private Long itemId;

    @JsonProperty("image_urls")
    private List<String> imageUrls;

    private String category;

    private String type;

    private Float threshold;

    @JsonProperty("top_k")
    private Integer topK;

    @Builder
    public AiMatchingRequest(Long itemId, List<String> imageUrls, String category, String type, Float threshold, Integer topK) {
        this.itemId = itemId;
        this.imageUrls = imageUrls;
        this.category = category;
        this.type = type;
        this.threshold = threshold;
        this.topK = topK;
    }
}
