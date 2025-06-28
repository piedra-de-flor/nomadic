package com.example.Triple_clone.domain.notification.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationEventService {

    private final List<NotificationSender> senders;

    public void notify(NotificationDto event) {
        for (NotificationSender sender : senders) {
            if (sender.supports(event.type())) {
                sender.process(event);
            }
        }
    }
}

