package com.capstonebe.capstonebe.user.dto.response;

import com.capstonebe.capstonebe.user.entity.RoleType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserLoginResponse {
    private final RoleType role;
    private final String token;
}
