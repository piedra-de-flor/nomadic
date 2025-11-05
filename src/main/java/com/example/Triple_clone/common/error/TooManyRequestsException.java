package com.example.Triple_clone.common.error;

import lombok.Getter;

@Getter
public class TooManyRequestsException extends RuntimeException {
    private final int retryAfterSeconds;

    public TooManyRequestsException(int retryAfterSeconds) {
        super("Too many requests");
        this.retryAfterSeconds = retryAfterSeconds;
    }
}