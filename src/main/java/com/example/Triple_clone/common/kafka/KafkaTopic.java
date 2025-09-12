package com.example.Triple_clone.common.kafka;

import lombok.Getter;

@Getter
public enum KafkaTopic {
    USER_NOTIFICATION_RETRY_TOPIC("user-notification-retry-topic"),
    USER_NOTIFICATION_DLQ_TOPIC("user-notification-dlq-topic"),
    EMAIL_RETRY_TOPIC("email-retry-topic"),
    EMAIL_DLQ_TOPIC("email-dlq-topic"),
    SLACK_RETRY_TOPIC("slack-retry-topic"),
    SLACK_DLQ_TOPIC("slack-dlq-topic");

    private final String value;

    KafkaTopic(String value) {
        this.value = value;
    }
}
