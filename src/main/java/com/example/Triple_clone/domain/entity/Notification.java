package com.example.Triple_clone.domain.entity;

import com.example.Triple_clone.domain.vo.NotificationTarget;
import com.example.Triple_clone.domain.vo.NotificationType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
}

