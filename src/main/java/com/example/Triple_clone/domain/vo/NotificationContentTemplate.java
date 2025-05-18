package com.example.Triple_clone.domain.vo;

public enum NotificationContentTemplate {
    REPORT("""
        신고 대상: %s (%d)
        사유: %s
        """);

    private final String template;

    NotificationContentTemplate(String template) {
        this.template = template;
    }

    public String format(Object... args) {
        return String.format(template, args);
    }
}
