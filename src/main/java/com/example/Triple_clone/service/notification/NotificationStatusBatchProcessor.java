package com.example.Triple_clone.service.notification;

import com.example.Triple_clone.domain.entity.NotificationStatus;
import com.example.Triple_clone.dto.notification.NotificationSentEvent;
import com.example.Triple_clone.repository.NotificationRepository;
import com.example.Triple_clone.repository.NotificationStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class NotificationStatusBatchProcessor {

    private final NotificationStatusQueue queue;
    private final NotificationRepository notificationRepository;
    private final NotificationStatusRepository statusRepository;

    @Scheduled(cron = "0 0 0 * * *")
    public void process() {
        List<NotificationSentEvent> events = queue.drainAll();
        for (NotificationSentEvent event : events) {
            notificationRepository.findById(event.notificationId())
                    .ifPresent(notification -> {
                        NotificationStatus status = NotificationStatus.builder()
                                .notification(notification)
                                .userId(event.memberId())
                                .build();
                        statusRepository.save(status);
                    });
        }
    }
}
