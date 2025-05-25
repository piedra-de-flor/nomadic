package com.example.Triple_clone.domain.vo;

import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public enum NotificationContentTemplate {
    REPORT("templates/report_notification_template.html");

    private final String template;

    NotificationContentTemplate(String template) {
        this.template = template;
    }

    public String loadReportEmailHtml(String reporterEmail, String targetType, Long targetId, String reason, String content) {
        try {
            ClassPathResource resource = new ClassPathResource(this.template);
            String html = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

            return html
                    .replace("{{reporterEmail}}", reporterEmail)
                    .replace("{{targetType}}", targetType)
                    .replace("{{targetId}}", String.valueOf(targetId))
                    .replace("{{reason}}", reason)
                    .replace("{{content}}", content);

        } catch (IOException e) {
            throw new RuntimeException("이메일 템플릿 로딩 실패", e);
        }
    }
}
