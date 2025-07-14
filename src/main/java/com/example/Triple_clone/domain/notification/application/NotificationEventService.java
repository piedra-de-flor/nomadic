package com.example.Triple_clone.domain.notification.application;

import com.example.Triple_clone.domain.notification.infra.NotificationSender;
import com.example.Triple_clone.domain.notification.web.dto.NotificationDto;
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

