package com.example.Triple_clone.service.notification;

import com.example.Triple_clone.domain.vo.NotificationType;
import com.example.Triple_clone.dto.notification.NotificationDto;

public interface NotificationSender {
    boolean supports(NotificationType type);
    void send(NotificationDto event);
}

