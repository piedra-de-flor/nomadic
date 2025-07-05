package com.example.Triple_clone.domain.notification.infra;

import com.example.Triple_clone.domain.notification.domain.NotificationChannelType;
import com.example.Triple_clone.domain.notification.web.dto.NotificationMessage;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

public interface ChannelNotificationSender {
    boolean supports(NotificationChannelType channel);
    void send(NotificationMessage message);
    NotificationChannelType getChannelType();
    MimeMessage makeMessage(NotificationMessage message) throws MessagingException;
}

