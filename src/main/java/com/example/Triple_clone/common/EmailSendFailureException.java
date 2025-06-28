package com.example.Triple_clone.common;

public class EmailSendFailureException extends RuntimeException {
    public EmailSendFailureException(String message, Throwable cause) {
        super(message, cause);
    }
}

