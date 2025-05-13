package com.capstonebe.capstonebe.post.dto.response;

import com.capstonebe.capstonebe.post.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class PostResponse {
    private final Long id;
    private final Long itemId;
    private final String title;
    private final String content;
    private final String imagePath;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static PostResponse from(Post post, String url) {
        return new PostResponse(
                post.getId(),
                post.getItem().getId(),
                post.getTitle(),
                post.getContent(),
                url,
                post.getCreatedAt(),
                post.getUpdatedAt()
        );
    }
}
