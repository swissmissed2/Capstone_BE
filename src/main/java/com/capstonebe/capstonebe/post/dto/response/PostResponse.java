package com.capstonebe.capstonebe.post.dto.response;

import com.capstonebe.capstonebe.post.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class PostResponse {
    private final long id;
    private final long itemId;
    private final String title;
    private final String content;

    public static PostResponse from(Post post) {
        return new PostResponse(
                post.getId(),
                post.getItem().getId(),
                post.getTitle(),
                post.getContent()
        );
    }
}
