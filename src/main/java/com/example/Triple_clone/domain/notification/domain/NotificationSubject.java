package com.example.Triple_clone.domain.notification.domain;

import lombok.Getter;

@Getter
public enum NotificationSubject {
    REPORT("[신고 알림] 새로운 신고 발생"),
    PLAN_SHARED("[계획 공유] 새로운 계획이 공유되었습니다"),
    PLAN_ACCEPTED("[계획 공유] 공유 요청이 수락되었습니다"),
    PLAN_REJECTED("[계획 공유] 공유 요청이 거절되었습니다");

    private final String value;

    NotificationSubject(String value) {
        this.value = value;
    }
}
