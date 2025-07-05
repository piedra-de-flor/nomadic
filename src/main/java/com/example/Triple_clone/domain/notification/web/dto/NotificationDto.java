package com.example.Triple_clone.domain.notification.web.dto;

import com.example.Triple_clone.domain.notification.domain.NotificationTarget;
import com.example.Triple_clone.domain.notification.domain.NotificationType;

public record NotificationDto(
    NotificationType type,
    NotificationTarget target,
    Object payload
) {}
