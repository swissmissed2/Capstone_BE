package com.capstonebe.capstonebe.post.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreatePostRequest {
    private Long itemId;

    @NotBlank
    private String title;

    @NotBlank
    private String content;
}
