package com.example.Triple_clone.domain.notification.notification;

import com.example.Triple_clone.domain.notification.Notification;
import com.example.Triple_clone.domain.notification.NotificationStatus;
import com.example.Triple_clone.domain.notification.NotificationRepository;
import com.example.Triple_clone.domain.notification.NotificationStatusRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationBatchProcessor {

    private final NotificationStatusQueue queue;
    private final NotificationRepository notificationRepository;
    private final NotificationStatusRepository statusRepository;

    @Transactional
    @Scheduled(cron = "0 0 0 * * *")
    public void saveNotificationStatus() {
        log.info("✅ Start Batch process = 알림 상태 저장");
        List<NotificationSentEvent> events = queue.drainAll();
        for (NotificationSentEvent event : events) {
            try {
                processEvent(event);
            } catch (Exception e) {
                log.error("❌ Failed to process notification event: {}", event, e);
                queue.enqueue(event);
            }
        }

        log.info("✅ End Batch process = 알림 상태 저장");
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void processEvent(NotificationSentEvent event) {
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

    @Transactional
    @Scheduled(cron = "0 50 23 * * *")
    public void deleteOldNotifications() {
        log.info("✅ Start Batch process = 오래된 알림 삭제");
        LocalDateTime threshold = LocalDateTime.now().minusDays(30);

        List<Notification> oldNotifications = notificationRepository.findBySentAtBefore(threshold);

        if (!oldNotifications.isEmpty()) {
            notificationRepository.deleteAll(oldNotifications);
        }

        log.info("✅ End Batch process = 오래된 알림 삭제");
    }
}
