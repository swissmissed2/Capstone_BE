package com.capstonebe.capstonebe.user.service;

import com.capstonebe.capstonebe.global.exception.CustomErrorCode;
import com.capstonebe.capstonebe.global.exception.CustomException;
import com.capstonebe.capstonebe.security.JwtUtil;
import com.capstonebe.capstonebe.user.dto.request.UserLoginRequest;
import com.capstonebe.capstonebe.user.dto.request.UserRegisterRequest;
import com.capstonebe.capstonebe.user.dto.request.UserUpdateRequest;
import com.capstonebe.capstonebe.user.dto.response.UserLoginResponse;
import com.capstonebe.capstonebe.user.dto.response.UserResponse;
import com.capstonebe.capstonebe.user.entity.RoleType;
import com.capstonebe.capstonebe.user.entity.User;
import com.capstonebe.capstonebe.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public UserResponse createUser(UserRegisterRequest registerRequest) {
        if(userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new CustomException(CustomErrorCode.DUPLICATED_EMAIL);
        }

        String encodedPassword = passwordEncoder.encode(registerRequest.getPassword());

        User user = User.builder()
                .role(RoleType.USER)
                .email(registerRequest.getEmail())
                .password(encodedPassword)
                .name(registerRequest.getName())
                .phone(registerRequest.getPhone())
                .isActive(true)
                .warnCount(0)
                .build();

        userRepository.save(user);
        return UserResponse.from(user);
    }

    @Transactional
    public UserResponse updateUser(String email, UserUpdateRequest updateRequest) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new CustomException(CustomErrorCode.USER_NOT_FOUND));

        if(updateRequest.getName() != null) {
            user.updateName(updateRequest.getName());
        }

        if(updateRequest.getPhone() != null) {
            user.updatePhone(updateRequest.getPhone());
        }

        return UserResponse.from(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new CustomException(CustomErrorCode.USER_NOT_FOUND);
        }

        userRepository.deleteById(id);
    }

    @Transactional
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new CustomException(CustomErrorCode.USER_NOT_FOUND));
    }

    @Transactional
    public UserLoginResponse login(UserLoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(() -> new CustomException(CustomErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new CustomException(CustomErrorCode.INVALID_PASSWORD);
        }

        if(!user.isActive()) {
            throw new CustomException(CustomErrorCode.USER_NOT_FOUND);
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());

        return new UserLoginResponse(user.getRole(), token);
    }

    @Transactional
    public UserResponse getUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new CustomException(CustomErrorCode.USER_NOT_FOUND));
        return UserResponse.from(user);
    }

    @Transactional(readOnly = true)
    public boolean isEmailDuplicate(String email) {
        return userRepository.existsByEmail(email);
    }

    @Transactional
    public boolean isAdmin(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new CustomException(CustomErrorCode.USER_NOT_FOUND));
        return user.getRole() == RoleType.ADMIN;
    }

    @Transactional(readOnly = true)
    public Page<UserResponse> getAllUsers(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        return users.map(UserResponse::from);
    }

    @Transactional
    public void activateUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new CustomException(CustomErrorCode.USER_NOT_FOUND));
        user.activate();
    }

    @Transactional
    public void deactivateUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new CustomException(CustomErrorCode.USER_NOT_FOUND));
        user.deactivate();
    }

    @Transactional(readOnly = true)
    public Page<UserResponse> searchUsers(String keyword, Pageable pageable) {
        Page<User> users = userRepository.findByNameContainingOrEmailContaining(keyword, keyword, pageable);
        return users.map(UserResponse::from);
    }
}
