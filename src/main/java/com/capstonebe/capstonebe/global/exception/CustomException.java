package com.capstonebe.capstonebe.global.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    private CustomErrorCode customErrorCode;

    public CustomException(CustomErrorCode customErrorCode) {
        super(customErrorCode.getMessage());
        this.customErrorCode = customErrorCode;
    }
}
