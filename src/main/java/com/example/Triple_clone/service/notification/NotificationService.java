package com.example.Triple_clone.service.notification;

import com.example.Triple_clone.domain.entity.Member;
import com.example.Triple_clone.domain.entity.Notification;
import com.example.Triple_clone.domain.entity.NotificationStatus;
import com.example.Triple_clone.domain.vo.NotificationTarget;
import com.example.Triple_clone.dto.notification.NotificationSearchDto;
import com.example.Triple_clone.repository.NotificationRepository;
import com.example.Triple_clone.repository.NotificationStatusRepository;
import com.example.Triple_clone.service.membership.UserService;
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
    private final UserService userService;

    @Transactional
    public List<NotificationSearchDto> getUserNotifications(String email) {
        Member member = userService.findByEmail(email);
        List<Notification> globalNotifications = notificationRepository.findByTarget(NotificationTarget.GLOBAL);
        List<Notification> personalNotifications = notificationRepository.findByTargetUserId(member.getId());

        List<Notification> all = new ArrayList<>();
        all.addAll(globalNotifications);
        all.addAll(personalNotifications);

        return all.stream()
                .map(notification -> {
                    boolean isRead = statusRepository.findByUserIdAndNotification(member.getId(), notification)
                            .map(NotificationStatus::isRead)
                            .orElse(false);

                    return NotificationSearchDto.from(notification, isRead);
                })
                .sorted(Comparator.comparing(NotificationSearchDto::sentAt).reversed())
                .toList();
    }

    @Transactional
    public void markAsRead(String email, Long notificationId) {
        Member member = userService.findByEmail(email);
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new IllegalArgumentException("알림 없음"));

        NotificationStatus status = statusRepository
                .findByUserIdAndNotification(member.getId(), notification)
                .orElseThrow(() -> new NoSuchElementException("상태값 없음"));

        status.read();
    }
}

