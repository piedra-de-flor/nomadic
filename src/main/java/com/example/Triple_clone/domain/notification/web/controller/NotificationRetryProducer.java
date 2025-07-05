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
public class NotificationRetryProducer {

    private final KafkaTemplate<String, NotificationMessage> kafkaTemplate;

    public void sendEmailRetryMessage(NotificationMessage message) {
        kafkaTemplate.send(KafkaTopic.EMAIL_RETRY_TOPIC.getValue(), message)
                .thenAccept(result -> {
                    log.info(NotificationLogMessage.KAFKA_PRODUCED.format(KafkaTopic.EMAIL_RETRY_TOPIC.getValue(), message.receiver()));
                })
                .exceptionally(e -> {
                    log.error(NotificationLogMessage.KAFKA_PRODUCE_FAILED.format(KafkaTopic.EMAIL_RETRY_TOPIC.getValue(), message.receiver(), e.getMessage()));
                    return null;
                });
    }

    public void sendSlackRetryMessage(NotificationMessage message) {
        kafkaTemplate.send(KafkaTopic.SLACK_RETRY_TOPIC.getValue(), message)
                .thenAccept(result -> {
                    log.info(NotificationLogMessage.KAFKA_PRODUCED.format(KafkaTopic.SLACK_RETRY_TOPIC.getValue(), message.receiver()));
                })
                .exceptionally(e -> {
                    log.error(NotificationLogMessage.KAFKA_PRODUCE_FAILED.format(KafkaTopic.SLACK_RETRY_TOPIC.getValue(), message.receiver(), e.getMessage()));
                    return null;
                });
    }
}


