package com.example.Triple_clone.domain.notification.notification.kafka;

import com.example.Triple_clone.common.KafkaTopic;
import com.example.Triple_clone.domain.notification.notification.NotificationMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationDlqProducer {
    private final KafkaTemplate<String, NotificationMessage> kafkaTemplate;

    public void sendEmailToDlq(NotificationMessage message) {
        kafkaTemplate.send(KafkaTopic.EMAIL_DLQ_TOPIC.getValue(), message);
    }

    public void sendSlackToDlq(NotificationMessage message) {
        kafkaTemplate.send(KafkaTopic.SLACK_DLQ_TOPIC.getValue(), message);
    }
}
