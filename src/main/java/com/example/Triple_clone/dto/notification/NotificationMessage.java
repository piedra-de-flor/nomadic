package com.example.Triple_clone.dto.notification;

import java.util.HashMap;
import java.util.Map;

public record NotificationMessage(
        String receiver,
        String subject,
        String content,
        Map<String, Object> metadata
) {
    public int getRetryCount() {
        Object value = metadata != null ? metadata.get("retryCount") : null;
        return value instanceof Integer ? (int) value : 0;
    }

    public NotificationMessage incrementRetryCount() {
        Map<String, Object> updated = metadata != null ? new HashMap<>(metadata) : new HashMap<>();
        updated.put("retryCount", getRetryCount() + 1);
        return new NotificationMessage(receiver, subject, content, updated);
    }
}

