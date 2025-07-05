package com.example.Triple_clone.domain.notification.web.dto;

import com.example.Triple_clone.domain.notification.domain.NotificationTarget;
import com.example.Triple_clone.domain.notification.domain.NotificationType;

import java.util.List;

public record NotificationSaveRequest(
        NotificationType type,
        NotificationTarget target,
        String title,
        String content,
        List<Long> targetUserIds
) {}
