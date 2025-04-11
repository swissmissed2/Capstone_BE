package com.capstonebe.capstonebe.category.dto.response;

import com.capstonebe.capstonebe.category.entity.Category;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CategoryResponse {

    String name;

    @Builder
    public CategoryResponse(String name) {
        this.name = name;
    }

    public static CategoryResponse fromEntity(Category category) {
        return CategoryResponse.builder().name(category.getName()).build();
    }
}
