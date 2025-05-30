package com.capstonebe.capstonebe.item.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class AiDescriptionRequest {

    @JsonProperty("image_urls")
    List<String> imageUrls;

    @Builder
    public AiDescriptionRequest(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }
}
