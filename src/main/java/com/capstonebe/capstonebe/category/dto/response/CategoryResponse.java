package com.capstonebe.capstonebe.category.dto.response;

import com.capstonebe.capstonebe.category.entity.Category;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CategoryResponse {

    Long id;

    String name;

    @Builder
    public CategoryResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static CategoryResponse fromEntity(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}
