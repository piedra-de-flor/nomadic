package com.example.Triple_clone.domain.notification;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class NotificationStatus {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Notification notification;

    private Long userId;

    private boolean isRead = false;

    private LocalDateTime readAt;

    @Builder
    public NotificationStatus(Notification notification, Long userId) {
        this.notification = notification;
        this.userId = userId;
    }

    public void read() {
        this.isRead = true;
        this.readAt = LocalDateTime.now();
    }
}
