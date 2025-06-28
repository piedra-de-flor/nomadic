package com.example.Triple_clone.common;

public class SlackSendFailureException extends RuntimeException {
    public SlackSendFailureException(String message, Throwable cause) {
        super(message, cause);
    }
}
