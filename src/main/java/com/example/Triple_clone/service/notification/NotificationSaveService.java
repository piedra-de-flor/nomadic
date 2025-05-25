package com.example.Triple_clone.service.notification;

import com.example.Triple_clone.domain.entity.Notification;
import com.example.Triple_clone.domain.entity.NotificationStatus;
import com.example.Triple_clone.dto.notification.NotificationSaveRequest;
import com.example.Triple_clone.repository.NotificationRepository;
import com.example.Triple_clone.repository.NotificationStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationSaveService {

    private final NotificationRepository notificationRepository;
    private final NotificationStatusRepository notificationStatusRepository;

    public void save(NotificationSaveRequest request) {
        for (Long userId : request.targetUserIds()) {
            Notification notification = Notification.builder()
                    .type(request.type())
                    .target(request.target())
                    .title(request.title())
                    .content(request.content())
                    .targetUserId(userId)
                    .build();

            notificationRepository.save(notification);

            NotificationStatus status = NotificationStatus.builder()
                    .notification(notification)
                    .userId(userId)
                    .build();

            notificationStatusRepository.save(status);
        }
    }
}