package com.example.Triple_clone.dto.notification;

import com.example.Triple_clone.domain.entity.Notification;

import java.time.LocalDateTime;

public record NotificationSearchDto(
        Long id,
        String title,
        String content,
        LocalDateTime sentAt,
        boolean isRead
) {
    public static NotificationSearchDto from(Notification notification, boolean isRead) {
        return new NotificationSearchDto(
                notification.getId(),
                notification.getTitle(),
                notification.getContent(),
                notification.getSentAt(),
                isRead
        );
    }
}