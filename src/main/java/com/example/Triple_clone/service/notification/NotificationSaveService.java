package com.example.Triple_clone.service.notification;

import com.example.Triple_clone.domain.entity.Notification;
import com.example.Triple_clone.domain.entity.NotificationStatus;
import com.example.Triple_clone.domain.vo.NotificationTarget;
import com.example.Triple_clone.domain.vo.NotificationType;
import com.example.Triple_clone.repository.NotificationRepository;
import com.example.Triple_clone.repository.NotificationStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NotificationSaveService {
    private final NotificationRepository notificationRepository;
    private final NotificationStatusRepository notificationStatusRepository;

    private void createNotification(NotificationType type, NotificationTarget target, Long targetUserId, String title, String content) {
        Notification notification = Notification.builder()
                .type(type)
                .target(target)
                .title(title)
                .content(content)
                .targetUserId(targetUserId)
                .build();

        notificationRepository.save(notification);

        NotificationStatus status = NotificationStatus.builder()
                .notification(notification)
                .userId(targetUserId)
                .build();

        notificationStatusRepository.save(status);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createGlobalNotification(NotificationType type, String title, String content) {
        createNotification(type, NotificationTarget.GLOBAL, null, title, content);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createPersonalNotification(NotificationType type, Long targetUserId, String title, String content) {
        createNotification(type, NotificationTarget.PERSONAL, targetUserId, title, content);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createAdminNotification(NotificationType type, Long targetUserId, String title, String content) {
        createNotification(type, NotificationTarget.ADMIN, targetUserId, title, content);
    }
}
