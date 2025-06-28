package com.example.Triple_clone.domain.notification.notification;

import com.example.Triple_clone.domain.notification.NotificationTarget;
import com.example.Triple_clone.domain.notification.NotificationType;

import java.util.List;

public record NotificationSaveRequest(
        NotificationType type,
        NotificationTarget target,
        String title,
        String content,
        List<Long> targetUserIds
) {}
