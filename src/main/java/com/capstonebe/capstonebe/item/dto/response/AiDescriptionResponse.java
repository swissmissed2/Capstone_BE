package com.capstonebe.capstonebe.item.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AiDescriptionResponse {
    private String name;
    private String description;
    private String category;
    private List<String> imageUrls;
}
