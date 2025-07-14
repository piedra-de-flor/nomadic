package com.example.Triple_clone.common.error;

public class SlackSendFailureException extends RuntimeException {
    public SlackSendFailureException(String message, Throwable cause) {
        super(message, cause);
    }
}
