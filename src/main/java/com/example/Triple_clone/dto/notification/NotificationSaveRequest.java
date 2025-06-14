package com.example.Triple_clone.dto.notification;

import com.example.Triple_clone.domain.vo.NotificationTarget;
import com.example.Triple_clone.domain.vo.NotificationType;

import java.util.List;

public record NotificationSaveRequest(
        NotificationType type,
        NotificationTarget target,
        String title,
        String content,
        List<Long> targetUserIds
) {}
