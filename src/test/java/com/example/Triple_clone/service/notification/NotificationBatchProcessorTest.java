package com.example.Triple_clone.service.notification;

import com.example.Triple_clone.domain.notification.domain.Notification;
import com.example.Triple_clone.domain.notification.domain.NotificationStatus;
import com.example.Triple_clone.domain.notification.domain.NotificationTarget;
import com.example.Triple_clone.domain.notification.domain.NotificationType;
import com.example.Triple_clone.batch.NotificationBatchProcessor;
import com.example.Triple_clone.domain.notification.web.dto.NotificationSentEvent;
import com.example.Triple_clone.domain.notification.infra.NotificationRepository;
import com.example.Triple_clone.domain.notification.infra.NotificationStatusRepository;
import java.util.concurrent.*;

import com.example.Triple_clone.domain.notification.infra.NotificationStatusQueue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class NotificationBatchProcessorTest {

    private NotificationRepository notificationRepository;
    private NotificationStatusRepository statusRepository;
    private NotificationStatusQueue queue;
    private NotificationBatchProcessor batchProcessor;

    @BeforeEach
    void setUp() {
        notificationRepository = mock(NotificationRepository.class);
        statusRepository = mock(NotificationStatusRepository.class);
        queue = mock(NotificationStatusQueue.class);

        batchProcessor = new NotificationBatchProcessor(queue, notificationRepository, statusRepository);
    }

    @Test
    @DisplayName("NotificationSentEvent를 처리하여 NotificationStatus 저장")
    void saveNotificationStatus() {
        Long notificationId = 1L;
        Long userId = 10L;
        NotificationSentEvent event = new NotificationSentEvent(notificationId, userId);

        Notification notification = Notification.builder()
                .type(NotificationType.REPORT_ALERT)
                .target(NotificationTarget.ADMIN)
                .title("Test Title")
                .content("Test Content")
                .targetUserId(null)
                .build();

        when(queue.drainAll()).thenReturn(List.of(event));
        when(notificationRepository.findById(notificationId)).thenReturn(Optional.of(notification));
        when(statusRepository.save(any(NotificationStatus.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        batchProcessor.saveNotificationStatus();

        verify(statusRepository, times(1)).save(any(NotificationStatus.class));
        verify(notificationRepository, times(1)).findById(notificationId);

        assertThat(notification.getStatuses()).hasSize(1);
        assertThat(notification.getStatuses().get(0).getUserId()).isEqualTo(userId);
    }

    @Test
    @DisplayName("30일 이상 지난 Notification을 삭제")
    void deleteOldNotifications() {
        Notification oldNotification = Notification.builder()
                .type(NotificationType.REPORT_ALERT)
                .target(NotificationTarget.ADMIN)
                .title("Old")
                .content("Old Content")
                .targetUserId(1L)
                .build();

        when(notificationRepository.findBySentAtBefore(any(LocalDateTime.class)))
                .thenReturn(List.of(oldNotification));

        batchProcessor.deleteOldNotifications();

        verify(notificationRepository, times(1)).deleteAll(List.of(oldNotification));
    }

    @Test
    @DisplayName("삭제할 오래된 Notification이 없는 경우")
    void deleteOldNotifications_emptyList() {
        when(notificationRepository.findBySentAtBefore(any(LocalDateTime.class)))
                .thenReturn(List.of());

        batchProcessor.deleteOldNotifications();

        verify(notificationRepository, never()).deleteAll(any());
    }

    @Test
    @DisplayName("멀티스레드 환경에서 enqueue와 drainAll 동시에 실행")
    void concurrentEnqueueAndDrainAllTest() throws Exception {
        NotificationStatusQueue realQueue = new NotificationStatusQueue();

        int totalEvents = 100;
        ExecutorService executor = Executors.newFixedThreadPool(2);

        Runnable producer = () -> {
            for (int i = 0; i < totalEvents; i++) {
                realQueue.enqueue(new NotificationSentEvent(1L, (long) i));
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        };

        Callable<List<NotificationSentEvent>> consumer = () -> {
            Thread.sleep(20);
            return realQueue.drainAll();
        };

        Future<?> producerFuture = executor.submit(producer);
        Future<List<NotificationSentEvent>> consumerFuture = executor.submit(consumer);

        producerFuture.get();
        List<NotificationSentEvent> drained = consumerFuture.get();
        int remaining = realQueue.drainAll().size();

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.SECONDS);

        int totalProcessed = drained.size() + remaining;

        System.out.println("drainAll()에서 가져온 이벤트 수: " + drained.size());
        System.out.println("drainAll 이후 남은 이벤트 수: " + remaining);
        System.out.println("총 생산된 이벤트 수: " + totalEvents);

        assertThat(totalProcessed).isEqualTo(totalEvents);
    }
}
