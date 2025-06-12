package com.example.Triple_clone.web.support;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import com.example.Triple_clone.domain.vo.NotificationChannelType;
import com.example.Triple_clone.dto.notification.NotificationMessage;
import com.example.Triple_clone.service.notification.channel.ChannelNotificationSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
            Map<String, Object> metadata = new HashMap<>();
            metadata.put("channelType", NotificationChannelType.SLACK.name());
            metadata.put("retryCount", 0);
            metadata.put("thread", eventObject.getThreadName());
            metadata.put("timestamp", eventObject.getTimeStamp());

            NotificationMessage message = new NotificationMessage(
                    "ADMIN",
                    "[ERROR] " + eventObject.getLoggerName(),
                    eventObject.getFormattedMessage(),
                    metadata
            );

            try {
                slackSender.send(message);
            } catch (Exception e) {
                log.warn("Slack 전송 실패 (LogbackAppender 내부): {}", e.getMessage());
            }
        }
    }
}


