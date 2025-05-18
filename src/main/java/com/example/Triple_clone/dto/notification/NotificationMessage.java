package com.example.Triple_clone.dto.notification;

import java.util.Map;

public record NotificationMessage(
        String receiver,
        String subject,
        String content,
        Map<String, Object> metadata
) {}

