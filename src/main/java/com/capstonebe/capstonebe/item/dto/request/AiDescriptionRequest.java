package com.capstonebe.capstonebe.item.dto.request;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class AiDescriptionRequest {

    List<String> imageUrls;

    @Builder
    public AiDescriptionRequest(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }
}
