package com.capstonebe.capstonebe.user.controller;

import com.capstonebe.capstonebe.comment.dto.response.CommentResponse;
import com.capstonebe.capstonebe.comment.service.CommentService;
import com.capstonebe.capstonebe.global.exception.CustomException;
import com.capstonebe.capstonebe.security.JwtUtil;
import com.capstonebe.capstonebe.user.dto.request.UserLoginRequest;
import com.capstonebe.capstonebe.user.dto.request.UserRegisterRequest;
import com.capstonebe.capstonebe.user.dto.request.UserUpdateRequest;
import com.capstonebe.capstonebe.user.dto.response.UserLoginResponse;
import com.capstonebe.capstonebe.user.dto.response.UserResponse;
import com.capstonebe.capstonebe.user.entity.RoleType;
import com.capstonebe.capstonebe.user.entity.User;
import com.capstonebe.capstonebe.user.service.UserService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final CommentService commentService;

    // 회원 가입
    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody @Valid UserRegisterRequest registerRequest) {
        UserResponse response = userService.createUser(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 회원 정보 수정
    @PutMapping("/update")
    public ResponseEntity<UserResponse> updateUser(@CookieValue(value = "jwt", required = false) String token, @RequestBody @Valid UserUpdateRequest updateRequest) {
        String email = jwtUtil.extractEmail(token);
        User updatedUser = userService.updateUser(email, updateRequest);
        return ResponseEntity.ok(UserResponse.from(updatedUser));
    }

    // 회원 탈퇴
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    // 단일 조회
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<Map<String, RoleType>> login(@RequestBody @Valid UserLoginRequest loginRequest, HttpServletResponse response) {
        try {
            UserLoginResponse loginResponse = userService.login(loginRequest);

            Cookie cookie = new Cookie("jwt", loginResponse.getToken());
            cookie.setHttpOnly(true);
            cookie.setSecure(false);
            cookie.setPath("/");
            cookie.setMaxAge(60 * 30);
            response.addCookie(cookie);

            return ResponseEntity.ok(Collections.singletonMap("role", loginResponse.getRole()));
        } catch (CustomException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    // 마이페이지
    @GetMapping("/myPage")
    public ResponseEntity<UserResponse> getMyPage(@CookieValue(value = "jwt", required = false) String token) {
        String email = jwtUtil.extractEmail(token);
        UserResponse userResponse = userService.getUserByEmail(email);
        return ResponseEntity.ok(userResponse);
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response, @CookieValue(value = "jwt", required = false) String token) {
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Cookie cookie = new Cookie("jwt", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return ResponseEntity.ok().build();
    }

    // 이메일 중복 체크
    @GetMapping("/check-email")
    public ResponseEntity<Boolean> checkEmailDuplicate(@RequestParam String email) {
        boolean isDuplicate = userService.isEmailDuplicate(email);
        return ResponseEntity.ok(isDuplicate);
    }

    // 권환 확인
    @GetMapping("/role")
    public ResponseEntity<Boolean> checkAdminRole(@CookieValue(value = "jwt", required = false) String token) {
        String email = jwtUtil.extractEmail(token);
        boolean isAdmin = userService.isAdmin(email);
        return ResponseEntity.ok(isAdmin);
    }

    // 회원 전체 조회
    @GetMapping
    public ResponseEntity<Page<UserResponse>> getAllUsers(Pageable pageable) {
        Page<UserResponse> users = userService.getAllUsers(pageable);
        return ResponseEntity.ok(users);
    }

    // 회원 활성화
    @PatchMapping("/{id}/activate")
    public ResponseEntity<Void> activateUser(@PathVariable Long id) {
        userService.activateUser(id);
        return ResponseEntity.ok().build();
    }

    // 회원 비활성화
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateUser(@PathVariable Long id) {
        userService.deactivateUser(id);
        return ResponseEntity.ok().build();
    }

    // 회원 검색
    @GetMapping("/search")
    public ResponseEntity<Page<UserResponse>> searchUsers(@RequestParam String keyword, Pageable pageable) {
        Page<UserResponse> users = userService.searchUsers(keyword, pageable);
        return ResponseEntity.ok(users);
    }

    // 유저 별 댓글 조회
    @GetMapping("/{id}/comments")
    public ResponseEntity<Page<CommentResponse>> getCommentsByUser(@PathVariable Long id, Pageable pageable) {
        Page<CommentResponse> comments = commentService.getCommentsByUser(id, pageable);
        return ResponseEntity.ok(comments);
    }
}
