package com.example.Triple_clone.repository;

import com.example.Triple_clone.domain.entity.Notification;
import com.example.Triple_clone.domain.vo.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByType(NotificationType type);
    List<Notification> findBySentAtBefore(LocalDateTime dateTime);
}
