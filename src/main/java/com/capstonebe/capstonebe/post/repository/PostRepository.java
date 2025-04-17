package com.capstonebe.capstonebe.post.repository;

import com.capstonebe.capstonebe.post.entity.Post;
import com.capstonebe.capstonebe.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findAll(Pageable pageable);
    Page<Post> findAllByUserId(Long userId, Pageable pageable);
}
