package com.example.Triple_clone.domain.notification.web.controller;

import com.example.Triple_clone.common.kafka.KafkaTopic;
import com.example.Triple_clone.common.logging.logMessage.NotificationLogMessage;
import com.example.Triple_clone.domain.notification.web.dto.NotificationMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationDlqProducer {

    private final KafkaTemplate<String, NotificationMessage> kafkaTemplate;

    public void sendEmailToDlq(NotificationMessage message) {
        kafkaTemplate.send(KafkaTopic.EMAIL_DLQ_TOPIC.getValue(), message)
                .thenAccept(result -> {
                    log.info(NotificationLogMessage.KAFKA_EVENT_TO_DLQ.format(KafkaTopic.EMAIL_DLQ_TOPIC.getValue(), message.receiver()));
                })
                .exceptionally(e -> {
                    log.error(NotificationLogMessage.KAFKA_EVENT_TO_DLQ_FAIL.format(KafkaTopic.EMAIL_DLQ_TOPIC.getValue(), message.receiver(), e.getMessage()));
                    return null;
                });
    }

    public void sendSlackToDlq(NotificationMessage message) {
        kafkaTemplate.send(KafkaTopic.SLACK_DLQ_TOPIC.getValue(), message)
                .thenAccept(result -> {
                    log.info(NotificationLogMessage.KAFKA_EVENT_TO_DLQ.format(KafkaTopic.SLACK_DLQ_TOPIC.getValue(), message.receiver()));
                })
                .exceptionally(e -> {
                    log.error(NotificationLogMessage.KAFKA_EVENT_TO_DLQ_FAIL.format(KafkaTopic.SLACK_DLQ_TOPIC.getValue(), message.receiver(), e.getMessage()));
                    return null;
                });
    }
}

