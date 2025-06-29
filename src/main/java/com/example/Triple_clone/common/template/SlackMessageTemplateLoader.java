package com.example.Triple_clone.common.template;

import com.example.Triple_clone.domain.notification.web.dto.NotificationMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j
@Component
public class SlackMessageTemplateLoader {
    private static final String TEMPLATE_PATH = "templates/slack-error-message-template.txt";
    private final String template;

    public SlackMessageTemplateLoader() {
        try (InputStream is = getClass().getClassLoader()
                .getResourceAsStream(TEMPLATE_PATH)) {
            if (is == null) {
                log.warn("Slack message 발송 실패 - Slack Template 조회 실패");
                throw new IllegalStateException("Slack 템플릿을 찾을 수 없습니다.");
            }
            this.template = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.warn("Slack message 발송 실패 - Slack Template 로딩 실패: {}", e.getMessage());
            throw new UncheckedIOException("Slack 템플릿 로딩 실패", e);
        }
    }

    public String build(NotificationMessage message) {
        Map<String, Object> m = message.metadata();

        return template
                .replace("{level}",     String.valueOf(m.getOrDefault("level", "ERROR")))
                .replace("{timestamp}", String.valueOf(m.getOrDefault("timestamp", "-")))
                .replace("{uri}",       String.valueOf(m.getOrDefault("uri", "-")))
                .replace("{email}",     String.valueOf(m.getOrDefault("email", "-")))
                .replace("{traceId}",   String.valueOf(m.getOrDefault("traceId", "-")))
                .replace("{content}",   message.content());
    }
}
