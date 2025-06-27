package com.example.Triple_clone.service.notification;

import com.example.Triple_clone.domain.member.Member;
import com.example.Triple_clone.domain.entity.Notification;
import com.example.Triple_clone.domain.entity.NotificationStatus;
import com.example.Triple_clone.dto.notification.NotificationSearchDto;
import com.example.Triple_clone.repository.NotificationQueryRepository;
import com.example.Triple_clone.repository.NotificationRepository;
import com.example.Triple_clone.repository.NotificationStatusRepository;
import com.example.Triple_clone.domain.member.UserService;
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
                    log.warn("⚠️ 알림 읽음 상태 변경 실패 - 존재 하지 않는 알림: {}", notificationId);
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
    }
}

