package com.capstonebe.capstonebe.comment.service;

import com.capstonebe.capstonebe.comment.dto.request.CreateCommentRequest;
import com.capstonebe.capstonebe.comment.dto.response.CommentResponse;
import com.capstonebe.capstonebe.comment.entity.Comment;
import com.capstonebe.capstonebe.comment.repository.CommentRepository;
import com.capstonebe.capstonebe.global.exception.CustomErrorCode;
import com.capstonebe.capstonebe.global.exception.CustomException;
import com.capstonebe.capstonebe.post.entity.Post;
import com.capstonebe.capstonebe.post.repository.PostRepository;
import com.capstonebe.capstonebe.user.entity.User;
import com.capstonebe.capstonebe.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Transactional
    public CommentResponse createComment(String email, CreateCommentRequest createRequest) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new CustomException(CustomErrorCode.USER_NOT_FOUND));
        Post post = postRepository.findById(createRequest.getPostId()).orElseThrow(() -> new CustomException(CustomErrorCode.POST_NOT_FOUND));

        Comment comment = Comment.builder()
                .user(user)
                .post(post)
                .content(post.getContent())
                .build();

        commentRepository.save(comment);
        return CommentResponse.from(comment);
    }

    @Transactional
    public void deleteComment(Long id) {
        if(!commentRepository.existsById(id)) {
            throw new CustomException(CustomErrorCode.COMMENT_NOT_FOUND);
        }

        commentRepository.deleteById(id);
    }

    @Transactional
    public Page<CommentResponse> getCommentsByPost(Long postId, Pageable pageable) {
        Page<Comment> comments = commentRepository.findAllByPostId(postId, pageable);
        return comments.map(CommentResponse::from);
    }

    @Transactional
    public Page<CommentResponse> getCommentsByUser(Long userId, Pageable pageable) {
        Page<Comment> comments = commentRepository.findAllByUserId(userId, pageable);
        return comments.map(CommentResponse::from);
    }
}
