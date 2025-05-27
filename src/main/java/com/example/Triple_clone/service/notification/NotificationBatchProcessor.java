package com.example.Triple_clone.service.notification;

import com.example.Triple_clone.domain.entity.Notification;
import com.example.Triple_clone.domain.entity.NotificationStatus;
import com.example.Triple_clone.dto.notification.NotificationSentEvent;
import com.example.Triple_clone.repository.NotificationRepository;
import com.example.Triple_clone.repository.NotificationStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class NotificationBatchProcessor {

    private final NotificationStatusQueue queue;
    private final NotificationRepository notificationRepository;
    private final NotificationStatusRepository statusRepository;

    @Transactional
    @Scheduled(cron = "0 0 0 * * *")
    public void saveNotificationStatus() {
        List<NotificationSentEvent> events = queue.drainAll();
        for (NotificationSentEvent event : events) {
            notificationRepository.findById(event.notificationId())
                    .ifPresent(notification -> {
                        NotificationStatus status = NotificationStatus.builder()
                                .notification(notification)
                                .userId(event.memberId())
                                .build();

                        statusRepository.save(status);
                        notification.addStatus(status);
                    });
        }
    }

    @Transactional
    @Scheduled(cron = "0 50 23 * * *")
    public void deleteOldNotifications() {
        LocalDateTime threshold = LocalDateTime.now().minusDays(30);

        List<Notification> oldNotifications = notificationRepository.findBySentAtBefore(threshold);

        if (!oldNotifications.isEmpty()) {
            notificationRepository.deleteAll(oldNotifications);
        }
    }
}
