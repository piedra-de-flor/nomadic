package com.example.Triple_clone.domain.notification.infra;

import com.example.Triple_clone.common.template.HtmlTemplateRenderer;
import com.example.Triple_clone.domain.notification.application.NotificationSaveService;
import com.example.Triple_clone.domain.notification.domain.NotificationChannelType;
import com.example.Triple_clone.domain.notification.domain.NotificationType;
import com.example.Triple_clone.domain.notification.web.dto.NotificationDto;
import com.example.Triple_clone.domain.notification.web.dto.NotificationSaveRequest;
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
