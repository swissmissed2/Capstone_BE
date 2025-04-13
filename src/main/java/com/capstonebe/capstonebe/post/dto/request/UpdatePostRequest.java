package com.capstonebe.capstonebe.post.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePostRequest {
    private Long itemId;
    private String title;
    private String content;
}
