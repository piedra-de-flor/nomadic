package com.example.Triple_clone.domain.entity;

import com.example.Triple_clone.domain.notification.domain.Notification;
import com.example.Triple_clone.domain.notification.domain.NotificationStatus;
import com.example.Triple_clone.domain.notification.domain.NotificationTarget;
import com.example.Triple_clone.domain.notification.domain.NotificationType;
import com.example.Triple_clone.domain.notification.infra.NotificationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class NotificationTest {

    @Autowired
    private NotificationRepository notificationRepository;

    @Test
    @DisplayName("Notification 생성 및 저장 테스트")
    void createAndSaveNotification() {
        Notification notification = Notification.builder()
                .type(NotificationType.REPORT_ALERT)
                .target(NotificationTarget.ADMIN)
                .title("신고 알림")
                .content("신고가 접수되었습니다.")
                .targetUserId(1L)
                .build();

        Notification saved = notificationRepository.save(notification);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getType()).isEqualTo(NotificationType.REPORT_ALERT);
        assertThat(saved.getTarget()).isEqualTo(NotificationTarget.ADMIN);
        assertThat(saved.getTitle()).isEqualTo("신고 알림");
        assertThat(saved.getContent()).isEqualTo("신고가 접수되었습니다.");
        assertThat(saved.getTargetUserId()).isEqualTo(1L);
        assertThat(saved.getSentAt()).isBeforeOrEqualTo(LocalDateTime.now());
    }

    @Test
    @DisplayName("NotificationStatus 연관관계 매핑 테스트")
    void addNotificationStatus() {
        Notification notification = Notification.builder()
                .type(NotificationType.REPORT_ALERT)
                .target(NotificationTarget.ADMIN)
                .title("신고 알림")
                .content("내용")
                .targetUserId(2L)
                .build();

        NotificationStatus status = new NotificationStatus(notification, 2L);
        notification.addStatus(status);

        Notification saved = notificationRepository.save(notification);

        assertThat(saved.getStatuses()).hasSize(1);
        assertThat(saved.getStatuses().get(0).getUserId()).isEqualTo(2L);
    }
}
