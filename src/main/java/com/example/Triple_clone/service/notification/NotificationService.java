package com.example.Triple_clone.service.notification;

import com.example.Triple_clone.domain.entity.Notification;
import com.example.Triple_clone.domain.entity.NotificationStatus;
import com.example.Triple_clone.domain.vo.NotificationTarget;
import com.example.Triple_clone.dto.notification.NotificationSearchDto;
import com.example.Triple_clone.repository.NotificationRepository;
import com.example.Triple_clone.repository.NotificationStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final NotificationStatusRepository statusRepository;

    @Transactional
    public List<NotificationSearchDto> getUserNotifications(Long userId) {
        List<Notification> globalNotifications = notificationRepository.findByTarget(NotificationTarget.GLOBAL);
        List<Notification> personalNotifications = notificationRepository.findByTargetUserId(userId);

        List<Notification> all = new ArrayList<>();
        all.addAll(globalNotifications);
        all.addAll(personalNotifications);

        return all.stream()
                .map(notification -> {
                    boolean isRead = statusRepository.findByUserIdAndNotification(userId, notification)
                            .map(NotificationStatus::isRead)
                            .orElse(false);

                    return NotificationSearchDto.from(notification, isRead);
                })
                .sorted(Comparator.comparing(NotificationSearchDto::sentAt).reversed())
                .toList();
    }

    @Transactional
    public void markAsRead(Long userId, Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new IllegalArgumentException("알림 없음"));

        NotificationStatus status = statusRepository
                .findByUserIdAndNotification(userId, notification)
                .orElseThrow(() -> new NoSuchElementException("상태값 없음"));

        status.read();
    }

    /**
     * 전체 알림 생성
     */
    public void createGlobalNotification(String title, String content) {
    }

    /**
     * 개인 알림 생성
     */
    public void createPersonalNotification(Long targetUserId, String title, String content) {
    }
}

