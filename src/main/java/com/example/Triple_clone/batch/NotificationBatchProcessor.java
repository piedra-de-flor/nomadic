package com.example.Triple_clone.batch;

import com.example.Triple_clone.common.logging.logMessage.BatchLogMessage;
import com.example.Triple_clone.domain.notification.domain.Notification;
import com.example.Triple_clone.domain.notification.domain.NotificationStatus;
import com.example.Triple_clone.domain.notification.infra.NotificationRepository;
import com.example.Triple_clone.domain.notification.infra.NotificationStatusQueue;
import com.example.Triple_clone.domain.notification.infra.NotificationStatusRepository;
import com.example.Triple_clone.domain.notification.web.dto.NotificationSentEvent;
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
        log.info(BatchLogMessage.BATCH_PROCESS_STARTED.format("알림 상태 저장"));
        List<NotificationSentEvent> events = queue.drainAll();
        for (NotificationSentEvent event : events) {
            try {
                processEvent(event);
            } catch (Exception e) {
                log.error(BatchLogMessage.BATCH_PROCESS_FAILED.format("알림 상태 저장 = " + event.notificationId(), e.getMessage()));
                queue.enqueue(event);
            }
        }

        log.info(BatchLogMessage.BATCH_PROCESS_ENDED.format("알림 상태 저장"));
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
        log.info(BatchLogMessage.BATCH_PROCESS_STARTED.format("오래된 알림 상태 삭제"));
        LocalDateTime threshold = LocalDateTime.now().minusDays(30);

        List<Notification> oldNotifications = notificationRepository.findBySentAtBefore(threshold);

        if (!oldNotifications.isEmpty()) {
            notificationRepository.deleteAll(oldNotifications);
        }

        log.info(BatchLogMessage.BATCH_PROCESS_ENDED.format("오래된 알림 상태 삭제"));
    }
}
