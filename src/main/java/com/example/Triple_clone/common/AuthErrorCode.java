package com.example.Triple_clone.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode{
    AUTH_ERROR_CODE(HttpStatus.FORBIDDEN, "deny access");

    private final HttpStatus httpStatus;
    private final String message;
}
