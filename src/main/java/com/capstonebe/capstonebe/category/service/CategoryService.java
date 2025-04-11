package com.capstonebe.capstonebe.category.service;

import com.capstonebe.capstonebe.category.dto.request.CategoryEditRequest;
import com.capstonebe.capstonebe.category.dto.request.CategoryRegisterRequest;
import com.capstonebe.capstonebe.category.dto.response.CategoryResponse;
import com.capstonebe.capstonebe.category.entity.Category;
import com.capstonebe.capstonebe.category.repository.CategoryRepository;
import com.capstonebe.capstonebe.global.exception.CustomErrorCode;
import com.capstonebe.capstonebe.global.exception.CustomException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional
    public CategoryResponse registerCategory(CategoryRegisterRequest request) {

        Category category = Category.builder()
                .name(request.getName())
                .build();

        categoryRepository.save(category);

        return CategoryResponse.fromEntity(category);
    }

    @Transactional
    public CategoryResponse editCategory(CategoryEditRequest request) {

        Category category = categoryRepository.findById(request.getId())
                .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_CATEGORY));

        category.updateName(request.getName());

        return CategoryResponse.fromEntity(category);
    }

    @Transactional
    public void deleteCategory(Long id) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_CATEGORY));

        categoryRepository.delete(category);
    }

    @Transactional(readOnly = true)
    public List<CategoryResponse> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();

        return categories.stream()
                .map(CategoryResponse::fromEntity)
                .toList();
    }


    // 테스트용
    @Transactional
    public void registerTestCategories() {
        List<Category> testCategories = List.of(
                new Category("전자기기"),
                new Category("의류"),
                new Category("서류/문서"),
                new Category("지갑/현금"),
                new Category("기타")
        );

        categoryRepository.saveAll(testCategories);
    }


}
