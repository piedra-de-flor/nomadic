package com.example.Triple_clone.domain.entity;

import com.example.Triple_clone.domain.vo.NotificationTarget;
import com.example.Triple_clone.domain.vo.NotificationType;
import com.example.Triple_clone.repository.NotificationRepository;
import com.example.Triple_clone.repository.NotificationStatusRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class NotificationStatusTest {
    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private NotificationStatusRepository notificationStatusRepository;

    @Test
    @DisplayName("NotificationStatus 생성 및 저장 테스트")
    void createAndSaveNotificationStatus() {
        Notification notification = Notification.builder()
                .type(NotificationType.REPORT_ALERT)
                .target(NotificationTarget.ADMIN)
                .title("신고 발생")
                .content("신고 내용입니다.")
                .targetUserId(1L)
                .build();

        Notification savedNotification = notificationRepository.save(notification);

        NotificationStatus status = NotificationStatus.builder()
                .notification(savedNotification)
                .userId(99L)
                .build();

        NotificationStatus savedStatus = notificationStatusRepository.save(status);

        assertThat(savedStatus.getId()).isNotNull();
        assertThat(savedStatus.getUserId()).isEqualTo(99L);
        assertThat(savedStatus.isRead()).isFalse();
        assertThat(savedStatus.getReadAt()).isNull();
        assertThat(savedStatus.getNotification().getId()).isEqualTo(savedNotification.getId());
    }

    @Test
    @DisplayName("NotificationStatus 읽음 처리 테스트")
    void markAsRead() {
        Notification notification = Notification.builder()
                .type(NotificationType.REPORT_ALERT)
                .target(NotificationTarget.ADMIN)
                .title("신고 발생")
                .content("신고 내용입니다.")
                .targetUserId(1L)
                .build();

        Notification savedNotification = notificationRepository.save(notification);

        NotificationStatus status = NotificationStatus.builder()
                .notification(savedNotification)
                .userId(99L)
                .build();

        NotificationStatus savedStatus = notificationStatusRepository.save(status);

        savedStatus.read();

        assertThat(savedStatus.isRead()).isTrue();
        assertThat(savedStatus.getReadAt()).isBeforeOrEqualTo(LocalDateTime.now());
    }
}
