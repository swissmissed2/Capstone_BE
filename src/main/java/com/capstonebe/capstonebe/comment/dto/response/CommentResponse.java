package com.capstonebe.capstonebe.comment.dto.response;

import com.capstonebe.capstonebe.comment.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CommentResponse {
    private final Long userId;
    private final String content;

    public static CommentResponse from(Comment comment) {
        return new CommentResponse(
                comment.getUser().getId(),
                comment.getContent()
        );
    }
}
