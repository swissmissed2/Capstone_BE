package com.capstonebe.capstonebe.post.repository;

import com.capstonebe.capstonebe.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface PostRepositoryCustom {
    Page<Post> searchPosts(Long categoryId, Long placeId, String keyword, LocalDate startDate, LocalDate endDate, Pageable pageable);
}