package com.example.Triple_clone.domain.notification.application;

import com.example.Triple_clone.common.logging.logMessage.NotificationLogMessage;
import com.example.Triple_clone.domain.member.domain.Member;
import com.example.Triple_clone.domain.member.application.UserService;
import com.example.Triple_clone.domain.notification.domain.Notification;
import com.example.Triple_clone.domain.notification.domain.NotificationStatus;
import com.example.Triple_clone.domain.notification.infra.NotificationQueryRepository;
import com.example.Triple_clone.domain.notification.infra.NotificationRepository;
import com.example.Triple_clone.domain.notification.infra.NotificationStatusRepository;
import com.example.Triple_clone.domain.notification.web.dto.NotificationSearchDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final NotificationQueryRepository notificationQueryRepository;
    private final NotificationStatusRepository statusRepository;
    private final UserService userService;

    @Transactional(readOnly = true)
    public List<NotificationSearchDto> getUserNotifications(String email) {
        Member member = userService.findByEmail(email);
        return notificationQueryRepository.findAllByUserId(member.getId());
    }

    @Transactional
    public void markAsRead(String email, Long notificationId) {
        Member member = userService.findByEmail(email);
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> {
                    log.warn(NotificationLogMessage.NOTIFICATION_READ_FAILED.format(notificationId));
                    return new IllegalArgumentException("알림 없음");
                });

        NotificationStatus status = statusRepository
                .findByUserIdAndNotification(member.getId(), notification)
                .orElseGet(() -> NotificationStatus
                        .builder()
                        .notification(notification)
                        .userId(member.getId())
                        .build());

        notification.addStatus(status);
        status.read();
        statusRepository.save(status);
    }
}

