package com.example.Triple_clone.domain.notification.notification.channel;

import com.example.Triple_clone.domain.notification.NotificationChannelType;
import com.example.Triple_clone.domain.notification.notification.NotificationMessage;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

public interface ChannelNotificationSender {
    boolean supports(NotificationChannelType channel);
    void send(NotificationMessage message);
    NotificationChannelType getChannelType();
    MimeMessage makeMessage(NotificationMessage message) throws MessagingException;
}

