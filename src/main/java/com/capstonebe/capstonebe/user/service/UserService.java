package com.capstonebe.capstonebe.user.service;

import com.capstonebe.capstonebe.global.exception.CustomErrorCode;
import com.capstonebe.capstonebe.global.exception.CustomException;
import com.capstonebe.capstonebe.user.dto.request.UserRegisterRequest;
import com.capstonebe.capstonebe.user.dto.response.UserResponse;
import com.capstonebe.capstonebe.user.entity.RoleType;
import com.capstonebe.capstonebe.user.entity.User;
import com.capstonebe.capstonebe.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponse addUser(UserRegisterRequest registerRequest) {
        if(userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new CustomException(CustomErrorCode.DUPLICATED_EMAIL);
        }

        String encodedPassword = passwordEncoder.encode(registerRequest.getPassword());

        User user = User.builder()
                .email(registerRequest.getEmail())
                .password(encodedPassword)
                .name(registerRequest.getName())
                .phone(registerRequest.getPhone())
                .role(RoleType.USER)
                .isActive(true)
                .build();

        userRepository.save(user);
        return UserResponse.from(user);
    }

}
