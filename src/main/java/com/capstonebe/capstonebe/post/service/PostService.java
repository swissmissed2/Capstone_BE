package com.capstonebe.capstonebe.post.service;

import com.capstonebe.capstonebe.comment.repository.CommentRepository;
import com.capstonebe.capstonebe.global.exception.CustomErrorCode;
import com.capstonebe.capstonebe.global.exception.CustomException;
import com.capstonebe.capstonebe.image.service.ImageService;
import com.capstonebe.capstonebe.item.entity.Item;
import com.capstonebe.capstonebe.item.repository.ItemRepository;
import com.capstonebe.capstonebe.keword.service.KeywordService;
import com.capstonebe.capstonebe.post.dto.request.CreatePostRequest;
import com.capstonebe.capstonebe.post.dto.request.UpdatePostRequest;
import com.capstonebe.capstonebe.post.dto.response.PostResponse;
import com.capstonebe.capstonebe.post.entity.Post;
import com.capstonebe.capstonebe.post.repository.PostRepository;
import com.capstonebe.capstonebe.user.entity.User;
import com.capstonebe.capstonebe.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final CommentRepository commentRepository;
    private final KeywordService keywordService;
    private final ImageService imageService;

    @Transactional
    public PostResponse createPost(String email, CreatePostRequest createRequest) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new CustomException(CustomErrorCode.USER_NOT_FOUND));
        Item item = itemRepository.findById(createRequest.getItemId()).orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_ITEM));

        Post post = Post.builder()
                .user(user)
                .item(item)
                .title(createRequest.getTitle())
                .content(createRequest.getContent())
                .build();

        postRepository.save(post);

        keywordService.sendKeywordMatchingNotify(post);

        String url = imageService.getImagePathByItemId(item.getId());

        return PostResponse.from(post, url);
    }

    @Transactional
    public PostResponse updatePost(String email, Long id, UpdatePostRequest updateRequest) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new CustomException(CustomErrorCode.USER_NOT_FOUND));
        Post post = postRepository.findById(id).orElseThrow(() -> new CustomException(CustomErrorCode.POST_NOT_FOUND));
        String url = imageService.getImagePathByItemId(post.getItem().getId());

        if (!post.getUser().getId().equals(user.getId())) {
            throw new CustomException(CustomErrorCode.NO_PERMISSION);
        }

        if(updateRequest.getItemId() != null) {
            Item item = itemRepository.findById(updateRequest.getItemId()).orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_ITEM));
            post.updateItem(item);
            url = imageService.getImagePathByItemId(item.getId());
        }

        boolean isUpdated = false;

        if (updateRequest.getTitle() != null) {
            post.updateTitle(updateRequest.getTitle());
            isUpdated = true;
        }

        if (updateRequest.getContent() != null) {
            post.updateContent(updateRequest.getContent());
            isUpdated = true;
        }

        if (isUpdated) {
            keywordService.sendKeywordMatchingNotify(post);
        }

        return PostResponse.from(post, url);
    }

    @Transactional
    public void deletePost(String email, Long id) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new CustomException(CustomErrorCode.USER_NOT_FOUND));
        Post post = postRepository.findById(id).orElseThrow(() -> new CustomException(CustomErrorCode.POST_NOT_FOUND));

        if (!post.getUser().getId().equals(user.getId())) {
            throw new CustomException(CustomErrorCode.NO_PERMISSION);
        }

        commentRepository.deleteAllByPostId(id);

        postRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<PostResponse> getAllPosts(Pageable pageable) {
        Page<Post> posts = postRepository.findAll(pageable);
        return posts.map(post -> {
            String url = imageService.getImagePathByItemId(post.getItem().getId());
            return PostResponse.from(post, url);
        });
    }

    @Transactional
    public PostResponse getPostById(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new CustomException(CustomErrorCode.POST_NOT_FOUND));
        String url = imageService.getImagePathByItemId(post.getItem().getId());
        return PostResponse.from(post, url);
    }

    @Transactional
    public Page<PostResponse> getPostsByUser(String email, Pageable pageable) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new CustomException(CustomErrorCode.USER_NOT_FOUND));
        Page<Post> posts = postRepository.findAllByUserId(user.getId(), pageable);
        return posts.map(post -> {
            String url = imageService.getImagePathByItemId(post.getItem().getId());
            return PostResponse.from(post, url);
        });
    }

    @Transactional(readOnly = true)
    public Page<PostResponse> searchPosts(Long categoryId, Long placeId, String keyword, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        Page<Post> posts = postRepository.searchPosts(categoryId, placeId, keyword, startDate, endDate, pageable);
        return posts.map(post -> {
            String url = imageService.getImagePathByItemId(post.getItem().getId());
            return PostResponse.from(post, url);
        });
    }
}
