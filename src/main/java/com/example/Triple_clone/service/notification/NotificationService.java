package com.example.Triple_clone.service.notification;

import com.example.Triple_clone.domain.entity.Notification;
import com.example.Triple_clone.domain.vo.NotificationTarget;
import com.example.Triple_clone.dto.notification.NotificationSearchDto;
import com.example.Triple_clone.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    /**
     * 유저에게 보여줄 알림 목록 (전체 + 개인)
     */
    public List<NotificationSearchDto> getUserNotifications(Long userId) {
        List<Notification> globalNotifications = notificationRepository.findByTarget(NotificationTarget.GLOBAL);
        List<Notification> personalNotifications = notificationRepository.findByTargetUserId(userId);

        List<Notification> all = new ArrayList<>();
        all.addAll(globalNotifications);
        all.addAll(personalNotifications);
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

