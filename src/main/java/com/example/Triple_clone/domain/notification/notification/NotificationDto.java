package com.example.Triple_clone.domain.notification.notification;

import com.example.Triple_clone.domain.notification.NotificationTarget;
import com.example.Triple_clone.domain.notification.NotificationType;

public record NotificationDto(
    NotificationType type,
    NotificationTarget target,
    Object payload
) {}
