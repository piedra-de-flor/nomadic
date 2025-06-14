package com.example.Triple_clone.dto.notification;

import com.example.Triple_clone.domain.vo.NotificationTarget;
import com.example.Triple_clone.domain.vo.NotificationType;

public record NotificationDto(
    NotificationType type,
    NotificationTarget target,
    Object payload
) {}
