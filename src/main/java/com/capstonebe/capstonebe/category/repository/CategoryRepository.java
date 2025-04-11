package com.capstonebe.capstonebe.category.repository;

import com.capstonebe.capstonebe.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
