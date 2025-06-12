package com.example.Triple_clone.service.notification.kafka;

import com.example.Triple_clone.domain.vo.KafkaTopic;
import com.example.Triple_clone.dto.notification.NotificationMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationRetryProducer {
    private final KafkaTemplate<String, NotificationMessage> kafkaTemplate;

    public void sendEmailRetryMessage(NotificationMessage message) {
        kafkaTemplate.send(KafkaTopic.EMAIL_RETRY_TOPIC.getValue(), message);
    }

    public void sendSlackRetryMessage(NotificationMessage message) {
        kafkaTemplate.send(KafkaTopic.SLACK_RETRY_TOPIC.getValue(), message);
    }
}
