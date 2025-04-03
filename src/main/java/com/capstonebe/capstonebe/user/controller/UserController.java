package com.capstonebe.capstonebe.user.controller;

import com.capstonebe.capstonebe.user.dto.request.UserRegisterRequest;
import com.capstonebe.capstonebe.user.dto.response.UserResponse;
import com.capstonebe.capstonebe.user.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody @Valid UserRegisterRequest registerRequest) {
        UserResponse response = userService.addUser(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
