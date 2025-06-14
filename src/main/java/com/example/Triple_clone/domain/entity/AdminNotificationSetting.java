package com.example.Triple_clone.domain.entity;

import com.example.Triple_clone.domain.vo.NotificationChannel;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdminNotificationSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id", nullable = false)
    private Member admin;

    @Embedded
    private NotificationChannel channel;

    @Column(nullable = false)
    private boolean notifyEveryReport;

    @Column(nullable = false)
    private int thresholdCount;

    @Builder
    public AdminNotificationSetting(Member admin, NotificationChannel channel, boolean notifyEveryReport, int thresholdCount) {
        this.admin = admin;
        this.channel = channel;
        this.notifyEveryReport = notifyEveryReport;
        this.thresholdCount = thresholdCount;
    }
}

