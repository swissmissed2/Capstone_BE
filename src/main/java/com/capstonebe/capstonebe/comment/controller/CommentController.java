package com.capstonebe.capstonebe.comment.controller;

import com.capstonebe.capstonebe.comment.dto.request.CreateCommentRequest;
import com.capstonebe.capstonebe.comment.dto.response.CommentResponse;
import com.capstonebe.capstonebe.comment.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // 댓글 작성
    @PostMapping
    public ResponseEntity<CommentResponse> createComment(@AuthenticationPrincipal UserDetails userDetails, @RequestBody @Valid CreateCommentRequest createRequest) {
        CommentResponse response = commentService.createComment(userDetails.getUsername(), createRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 댓글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }
}
