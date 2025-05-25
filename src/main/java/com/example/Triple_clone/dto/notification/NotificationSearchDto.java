package com.example.Triple_clone.dto.notification;

import java.time.LocalDateTime;

public record NotificationSearchDto(
        Long id,
        String title,
        String content,
        LocalDateTime sentAt,
        boolean isRead
) {}