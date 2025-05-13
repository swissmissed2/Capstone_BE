package com.capstonebe.capstonebe.category.controller;

import com.capstonebe.capstonebe.category.dto.request.CategoryEditRequest;
import com.capstonebe.capstonebe.category.dto.request.CategoryRegisterRequest;
import com.capstonebe.capstonebe.category.dto.response.CategoryResponse;
import com.capstonebe.capstonebe.category.service.CategoryService;
import com.capstonebe.capstonebe.global.exception.CustomErrorCode;
import com.capstonebe.capstonebe.global.exception.CustomException;
import com.capstonebe.capstonebe.security.JwtUtil;
import com.capstonebe.capstonebe.user.service.UserService;
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
    private final UserService userService;
    private final JwtUtil jwtUtil;

    // 카테고리 등록
    @PostMapping("/register")
    public ResponseEntity<?> registerCategory(@RequestBody @Valid CategoryRegisterRequest request,
                                              @CookieValue(value = "jwt", required = false) String token) {

        //validateAdmin(token);

        return ResponseEntity.ok(categoryService.registerCategory(request));
    }

    // 카테고리 수정
    @PatchMapping("/edit")
    public ResponseEntity<?> editCategory(@RequestBody @Valid CategoryEditRequest request,
                                          @CookieValue(value = "jwt", required = false) String token) {

        //validateAdmin(token);

        return ResponseEntity.ok(categoryService.editCategory(request));
    }

    // 카테고리 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id,
                                            @CookieValue(value = "jwt", required = false) String token) {

        //validateAdmin(token);

        categoryService.deleteCategory(id);

        return ResponseEntity.ok().build();
    }

    // 카테고리 전체 목록 조회
    @GetMapping
    public ResponseEntity<?> getAllCategories() {
        List<CategoryResponse> response = categoryService.getAllCategories();
        return ResponseEntity.ok(response);
    }

    private void validateAdmin(String token) {

        if (token == null || !userService.isAdmin(jwtUtil.extractEmail(token))) {
            throw new CustomException(CustomErrorCode.IS_NOT_ADMIN);
        }
    }

}
