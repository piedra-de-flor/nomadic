package com.example.Triple_clone.service.notification.channel;

import com.example.Triple_clone.domain.vo.NotificationChannelType;
import com.example.Triple_clone.dto.notification.NotificationMessage;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

public interface ChannelNotificationSender {
    boolean supports(NotificationChannelType channel);
    void send(NotificationMessage message);
    NotificationChannelType getChannelType();

    public MimeMessage makeMessage(NotificationMessage message) throws MessagingException;
}

