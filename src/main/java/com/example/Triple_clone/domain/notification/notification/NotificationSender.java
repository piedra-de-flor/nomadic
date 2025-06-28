package com.example.Triple_clone.domain.notification.notification;

import com.example.Triple_clone.domain.notification.NotificationChannelType;
import com.example.Triple_clone.domain.notification.NotificationType;
import com.example.Triple_clone.domain.notification.notification.channel.ChannelNotificationSender;
import com.example.Triple_clone.common.HtmlTemplateRenderer;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public abstract class NotificationSender {
    protected final HtmlTemplateRenderer htmlTemplateRenderer;
    protected final List<ChannelNotificationSender> channelSenders;
    protected final NotificationSaveService notificationSaveService;

    public final void process(NotificationDto dto) {
        NotificationSaveRequest saveRequest = prepareAndSend(dto);
        notificationSaveService.save(saveRequest);
    }

    protected abstract NotificationSaveRequest prepareAndSend(NotificationDto dto);

    public abstract boolean supports(NotificationType type);

    protected NotificationChannelType resolveChannelType(ChannelNotificationSender sender) {
        return sender.getChannelType();
    }
}
