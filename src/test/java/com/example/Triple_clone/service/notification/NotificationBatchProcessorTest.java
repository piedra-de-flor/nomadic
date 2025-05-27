package com.example.Triple_clone.service.notification;

import com.example.Triple_clone.domain.entity.Notification;
import com.example.Triple_clone.domain.entity.NotificationStatus;
import com.example.Triple_clone.domain.vo.NotificationTarget;
import com.example.Triple_clone.domain.vo.NotificationType;
import com.example.Triple_clone.dto.notification.NotificationSentEvent;
import com.example.Triple_clone.repository.NotificationRepository;
import com.example.Triple_clone.repository.NotificationStatusRepository;
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
}
