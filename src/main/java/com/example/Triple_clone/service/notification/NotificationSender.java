package com.example.Triple_clone.service.notification;

import com.example.Triple_clone.domain.vo.NotificationChannelType;
import com.example.Triple_clone.domain.vo.NotificationType;
import com.example.Triple_clone.dto.notification.NotificationDto;
import com.example.Triple_clone.service.notification.channel.ChannelNotificationSender;
import com.example.Triple_clone.web.support.HtmlTemplateRenderer;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public abstract class NotificationSender {
    protected final HtmlTemplateRenderer htmlTemplateRenderer;
    protected final List<ChannelNotificationSender> channelSenders;

    abstract boolean supports(NotificationType type);

    abstract void send(NotificationDto event);

    public NotificationChannelType resolveChannelType(ChannelNotificationSender sender) {
        return sender.getChannelType();
    }
}

