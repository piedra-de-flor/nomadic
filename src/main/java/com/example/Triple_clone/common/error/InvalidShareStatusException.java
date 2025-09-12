package com.example.Triple_clone.common.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum InvalidShareStatusException implements ErrorCode{
    INVALID_SHARE_STATUS_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "PENDING 상태일 때만 수락 혹은 거절이 가능합니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
