package com.example.Triple_clone.service.notification.channel;

import com.example.Triple_clone.domain.vo.NotificationChannelType;
import com.example.Triple_clone.dto.notification.NotificationMessage;
import org.springframework.stereotype.Component;

@Component
public class SlackNotificationSender implements ChannelNotificationSender {

    @Override
    public boolean supports(NotificationChannelType channel) {
        return channel == NotificationChannelType.SLACK;
    }

    @Override
    public void send(NotificationMessage message) {
        System.out.printf("[SLACK] To: %s | Subject: %s | Content: %s%n",
                message.receiverName(), message.subject(), message.content());
    }
}
