package com.example.Triple_clone.domain.notification.domain;

import com.example.Triple_clone.domain.notification.domain.NotificationStatus;
import com.example.Triple_clone.domain.notification.domain.NotificationTarget;
import com.example.Triple_clone.domain.notification.domain.NotificationType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    @Enumerated(EnumType.STRING)
    private NotificationTarget target;

    @OneToMany(mappedBy = "notification", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<NotificationStatus> statuses = new ArrayList<>();

    @Column(nullable = false)
    private String title;

    @Lob
    private String content;

    private LocalDateTime sentAt;

    private Long targetUserId;

    @PrePersist
    public void prePersist() {
        this.sentAt = LocalDateTime.now();
    }

    @Builder
    public Notification(NotificationType type, NotificationTarget target, String title, String content, Long targetUserId) {
        this.type = type;
        this.target = target;
        this.title = title;
        this.content = content;
        this.targetUserId = targetUserId;
    }

    public void addStatus(NotificationStatus notificationStatus) {
        statuses.add(notificationStatus);
    }
}

