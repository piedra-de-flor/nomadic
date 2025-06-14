package com.example.Triple_clone.web.support;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import com.example.Triple_clone.domain.vo.NotificationChannelType;
import com.example.Triple_clone.dto.notification.NotificationMessage;
import com.example.Triple_clone.service.notification.channel.ChannelNotificationSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class SlackLogbackAppender extends AppenderBase<ILoggingEvent> {
    private ChannelNotificationSender slackSender;

    public void setSlackSender(ChannelNotificationSender slackSender) {
        this.slackSender = slackSender;
    }

    @Override
    protected void append(ILoggingEvent eventObject) {
        if (eventObject.getLevel().isGreaterOrEqual(Level.ERROR)) {
            try {
                String logMessage = eventObject.getFormattedMessage();
                if (logMessage == null || logMessage.isBlank()) {
                    logMessage = "No message content.";
                }

                String timestamp = Instant.ofEpochMilli(eventObject.getTimeStamp())
                        .atZone(ZoneId.of("Asia/Seoul"))
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

                NotificationMessage message = buildNotificationMessage(eventObject, timestamp, logMessage);

                slackSender.send(message);

            } catch (Exception e) {
                log.warn("Slack 전송 실패 (LogbackAppender 내부): {}", e.getMessage(), e);
            }
        }
    }

    private static NotificationMessage buildNotificationMessage(ILoggingEvent eventObject, String timestamp, String logMessage) {
        Map<String, String> mdcMap = eventObject.getMDCPropertyMap();
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("channelType", NotificationChannelType.SLACK.name());
        metadata.put("retryCount", 0);
        metadata.put("thread", eventObject.getThreadName());
        metadata.put("timestamp", timestamp);
        metadata.put("traceId", mdcMap.getOrDefault("traceId", "-"));
        metadata.put("email", mdcMap.getOrDefault("email", "-"));
        metadata.put("uri", mdcMap.getOrDefault("uri", "-"));
        metadata.put("level", eventObject.getLevel().levelStr);
        metadata.put("logger", eventObject.getLoggerName());

        NotificationMessage message = new NotificationMessage(
                "ADMIN",
                "[ERROR] " + eventObject.getLoggerName(),
                logMessage,
                metadata
        );
        return message;
    }
}


