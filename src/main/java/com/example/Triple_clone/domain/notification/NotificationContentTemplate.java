package com.example.Triple_clone.domain.notification;

import lombok.Getter;

@Getter
public enum NotificationContentTemplate {
    REPORT("report_notification_template.html");

    private final String path;

    NotificationContentTemplate(String path) {
        this.path = path;
    }
}
