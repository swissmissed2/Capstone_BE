package com.capstonebe.capstonebe.image.repository;

import com.capstonebe.capstonebe.image.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {
    Optional<Image> findPathById(Long id);
    Optional<Image> findByItemId(Long itemId);
}
