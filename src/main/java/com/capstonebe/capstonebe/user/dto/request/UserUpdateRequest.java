package com.capstonebe.capstonebe.user.dto.request;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserUpdateRequest {
    @Size(max = 25, message = "25자 이하로 작성해 주세요")
    @Pattern(regexp = "^[가-힣]*$", message = "한글만 입력해주세요")
    private String name;

    @Pattern(regexp = "^010\\d{8}$", message = "'-' 없이 올바른 전화번호를 입력해 주세요")
    private String phone;
}
