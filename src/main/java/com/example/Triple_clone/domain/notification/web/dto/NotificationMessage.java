package com.example.Triple_clone.domain.notification.web.dto;

import java.util.HashMap;
import java.util.Map;

public record NotificationMessage(
        String receiver,
        String subject,
        String content,
        Map<String, Object> metadata
) {
    public static final String RETRY_COUNT_KEY = "retryCount";

    public int getRetryCount() {
        Object value = metadata != null ? metadata.get(RETRY_COUNT_KEY) : null;
        return value instanceof Integer ? (int) value : 0;
    }

    public NotificationMessage incrementRetryCount() {
        Map<String, Object> updated = metadata != null ? new HashMap<>(metadata) : new HashMap<>();
        updated.put("retryCount", getRetryCount() + 1);
        return new NotificationMessage(receiver, subject, content, updated);
    }
}

