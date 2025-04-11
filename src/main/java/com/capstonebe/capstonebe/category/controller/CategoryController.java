package com.capstonebe.capstonebe.category.controller;

import com.capstonebe.capstonebe.category.dto.request.CategoryEditRequest;
import com.capstonebe.capstonebe.category.dto.request.CategoryRegisterRequest;
import com.capstonebe.capstonebe.category.dto.response.CategoryResponse;
import com.capstonebe.capstonebe.category.service.CategoryService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@AllArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/register")
    public ResponseEntity<?> registerCategory(@RequestBody @Valid CategoryRegisterRequest request) {
        return ResponseEntity.ok(categoryService.registerCategory(request));
    }

    @PatchMapping("/edit")
    public ResponseEntity<?> editCategory(@RequestBody @Valid CategoryEditRequest request) {
        return ResponseEntity.ok(categoryService.editCategory(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<?> getAllCategories() {
        List<CategoryResponse> response = categoryService.getAllCategories();
        return ResponseEntity.ok(response);
    }



    // 테스트용
    @PostMapping("/test-bulk-register")
    public ResponseEntity<String> registerTestCategories() {
        categoryService.registerTestCategories();
        return ResponseEntity.ok("테스트용 카테고리 5개 등록 완료");
    }

}
