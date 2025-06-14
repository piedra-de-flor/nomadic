package com.example.Triple_clone.service.notification;

import com.example.Triple_clone.domain.entity.Member;
import com.example.Triple_clone.domain.entity.Notification;
import com.example.Triple_clone.domain.entity.NotificationStatus;
import com.example.Triple_clone.domain.vo.NotificationTarget;
import com.example.Triple_clone.dto.notification.NotificationSearchDto;
import com.example.Triple_clone.repository.NotificationQueryRepository;
import com.example.Triple_clone.repository.NotificationRepository;
import com.example.Triple_clone.repository.NotificationStatusRepository;
import com.example.Triple_clone.service.membership.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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
                .orElseThrow(() -> new IllegalArgumentException("알림 없음"));

        NotificationStatus status = statusRepository
                .findByUserIdAndNotification(member.getId(), notification)
                .orElseGet(() -> NotificationStatus
                        .builder()
                        .notification(notification)
                        .userId(member.getId())
                        .build());

        notification.addStatus(status);
        status.read();
    }
}

