package com.capstonebe.capstonebe.user.dto.response;

import com.capstonebe.capstonebe.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UserResponse {
    private final long id;
    private final String email;
    private final String name;
    private final String phone;
    private final boolean isActive;

    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getPhone(),
                user.isActive()
        );
    }
}