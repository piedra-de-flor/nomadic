package com.example.Triple_clone.common.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum DuplicatedProcessErrorCode implements ErrorCode{
    DUPLICATED_PROCESS_ERROR_CODE(HttpStatus.BAD_REQUEST, "Duplicated Process");

    private final HttpStatus httpStatus;
    private final String message;
}
