package com.example.Triple_clone.domain.notification;

import lombok.Getter;

@Getter
public enum NotificationSubject {
    REPORT("[신고 알림] 새로운 신고 발생");

    private final String value;

    NotificationSubject(String value) {
        this.value = value;
    }
}
