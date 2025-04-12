package com.capstonebe.capstonebe.post.service;

import com.capstonebe.capstonebe.global.exception.CustomErrorCode;
import com.capstonebe.capstonebe.global.exception.CustomException;
import com.capstonebe.capstonebe.item.entity.Item;
import com.capstonebe.capstonebe.item.repository.ItemRepository;
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

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

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
        return PostResponse.from(post);
    }

    @Transactional
    public PostResponse updatePost(Long id, UpdatePostRequest updateRequest) {
        Post post = postRepository.findById(id).orElseThrow(() -> new CustomException(CustomErrorCode.POST_NOT_FOUND));

        if(updateRequest.getItemId() != null) {
            Item item = itemRepository.findById(updateRequest.getItemId()).orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_ITEM));
            post.updateItem(item);
        }

        if (updateRequest.getTitle() != null) {
            post.updateTitle(updateRequest.getTitle());
        }

        if (updateRequest.getContent() != null) {
            post.updateContent(updateRequest.getContent());
        }

        return PostResponse.from(post);
    }

    @Transactional
    public void deletePost(Long id) {
        if (!postRepository.existsById(id)) {
            throw new CustomException(CustomErrorCode.POST_NOT_FOUND);
        }

        postRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<PostResponse> getAllPosts(Pageable pageable) {
        Page<Post> posts = postRepository.findAll(pageable);
        return posts.map(PostResponse::from);
    }
}
