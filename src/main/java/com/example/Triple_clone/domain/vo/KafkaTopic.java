package com.example.Triple_clone.domain.vo;

import lombok.Getter;

@Getter
public enum KafkaTopic {
    EMAIL_RETRY_TOPIC("email-retry-topic"),
    EMAIL_DLQ_TOPIC("email-dlq-topic");

    private final String value;

    KafkaTopic(String value) {
        this.value = value;
    }
}
