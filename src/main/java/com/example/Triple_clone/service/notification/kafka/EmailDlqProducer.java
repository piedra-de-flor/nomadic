package com.example.Triple_clone.service.notification.kafka;

import com.example.Triple_clone.domain.vo.KafkaTopic;
import com.example.Triple_clone.dto.notification.NotificationMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailDlqProducer {
    private final KafkaTemplate<String, NotificationMessage> kafkaTemplate;

    public void sendToDlq(NotificationMessage message) {
        kafkaTemplate.send(KafkaTopic.EMAIL_DLQ_TOPIC.getValue(), message);
    }
}
