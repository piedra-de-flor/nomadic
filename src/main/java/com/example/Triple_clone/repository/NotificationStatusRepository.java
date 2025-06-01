package com.example.Triple_clone.repository;

import com.example.Triple_clone.domain.entity.Notification;
import com.example.Triple_clone.domain.entity.NotificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotificationStatusRepository extends JpaRepository<NotificationStatus, Long> {
    Optional<NotificationStatus> findByUserIdAndNotification(Long userId, Notification notification);
    List<NotificationStatus> findByUserId(Long userId);
}
