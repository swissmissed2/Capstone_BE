package com.capstonebe.capstonebe.post.controller;

import com.capstonebe.capstonebe.comment.dto.response.CommentResponse;
import com.capstonebe.capstonebe.comment.service.CommentService;
import com.capstonebe.capstonebe.post.dto.request.CreatePostRequest;
import com.capstonebe.capstonebe.post.dto.request.UpdatePostRequest;
import com.capstonebe.capstonebe.post.dto.response.PostResponse;
import com.capstonebe.capstonebe.post.entity.Post;
import com.capstonebe.capstonebe.post.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final CommentService commentService;

    // 포스트 작성
    @PostMapping
    public ResponseEntity<PostResponse> createPost(@AuthenticationPrincipal UserDetails userDetails, @RequestBody @Valid CreatePostRequest createRequest) {
        PostResponse response = postService.createPost(userDetails.getUsername(), createRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 포스트 수정
    @PatchMapping("/update/{id}")
    public ResponseEntity<PostResponse> updatePost(@PathVariable Long id, @RequestBody UpdatePostRequest updateRequest) {
        PostResponse response = postService.updatePost(id, updateRequest);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 포스트 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }

    // 포스트 전체 조회
    @GetMapping
    public ResponseEntity<Page<PostResponse>> getAllPosts(Pageable pageable) {
        Page<PostResponse> posts = postService.getAllPosts(pageable);
        return ResponseEntity.ok(posts);
    }

    // 포스트 단일 조회
    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPost(Long id) {
        PostResponse response = postService.getPostById(id);
        return ResponseEntity.ok(response);
    }

    // 포스트 별 댓글 조회
    @GetMapping("/{id}/comments")
    public ResponseEntity<Page<CommentResponse>> getCommentsByPost(@PathVariable Long id, Pageable pageable) {
        Page<CommentResponse> comments = commentService.getCommentsByPost(id, pageable);
        return ResponseEntity.ok(comments);
    }
}
