package com.example.Triple_clone.web.exception;

public class SlackSendFailureException extends RuntimeException {
    public SlackSendFailureException(String message, Throwable cause) {
        super(message, cause);
    }
}
