package com.example.Triple_clone.common.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum JsonProcessingErrorCode implements ErrorCode{
    JSON_PROCESSING_ERROR_CODE(HttpStatus.INTERNAL_SERVER_ERROR, "변경 데이터 직렬화 실패");

    private final HttpStatus httpStatus;
    private final String message;
}
