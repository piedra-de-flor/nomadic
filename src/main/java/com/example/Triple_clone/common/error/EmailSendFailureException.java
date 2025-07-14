package com.example.Triple_clone.common.error;

public class EmailSendFailureException extends RuntimeException {
    public EmailSendFailureException(String message, Throwable cause) {
        super(message, cause);
    }
}

