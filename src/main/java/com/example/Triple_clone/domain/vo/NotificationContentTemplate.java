package com.example.Triple_clone.domain.vo;

import lombok.Getter;

@Getter
public enum NotificationContentTemplate {
    REPORT("templates/report_notification_template.html");

    private final String path;

    NotificationContentTemplate(String path) {
        this.path = path;
    }
}
