package com.example.Triple_clone.service.notification.channel;

import com.example.Triple_clone.domain.vo.NotificationChannelType;
import com.example.Triple_clone.dto.notification.NotificationMessage;

public interface ChannelNotificationSender {
    boolean supports(NotificationChannelType channel);
    void send(NotificationMessage message);
}

