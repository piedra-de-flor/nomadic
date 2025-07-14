package com.example.Triple_clone.domain.notification.web.controller;

import com.example.Triple_clone.common.kafka.KafkaTopic;
import com.example.Triple_clone.common.logging.logMessage.NotificationLogMessage;
import com.example.Triple_clone.domain.notification.web.dto.NotificationMessage;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(value = "kafka.enabled", havingValue = "true", matchIfMissing = true)
public class NotificationRetryConsumer {
    private final JavaMailSender mailSender;
    private final NotificationRetryProducer notificationRetryProducer;
    private final NotificationDlqProducer notificationDlqProducer;

    private static final int MAX_RETRY_COUNT = 3;

    @KafkaListener(topics = "email-retry-topic", groupId = "email-retry-group")
    public void retrySendEmail(NotificationMessage message) {
        int retryCount = message.getRetryCount();

        if (retryCount >= MAX_RETRY_COUNT) {
            notificationDlqProducer.sendEmailToDlq(message);
            log.warn(NotificationLogMessage.KAFKA_EVENT_TO_DLQ.format(KafkaTopic.EMAIL_DLQ_TOPIC.getValue(), message.receiver()));
            return;
        }

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            helper.setTo(message.receiver());
            helper.setSubject(message.subject());
            helper.setText(message.content(), true);
            mailSender.send(mimeMessage);
        } catch (Exception e) {
            log.warn(NotificationLogMessage.KAFKA_RETRY_FAILED.format(KafkaTopic.EMAIL_RETRY_TOPIC.getValue(), message.receiver(), retryCount + 1));
            notificationRetryProducer.sendEmailRetryMessage(message.incrementRetryCount());
        }
    }

    @KafkaListener(topics = "slack-retry-topic", groupId = "slack-retry-group")
    public void retrySendSlack(NotificationMessage message) {
        int retryCount = message.getRetryCount();

        if (retryCount >= MAX_RETRY_COUNT) {
            notificationDlqProducer.sendSlackToDlq(message);
            log.warn(NotificationLogMessage.KAFKA_EVENT_TO_DLQ.format(KafkaTopic.SLACK_DLQ_TOPIC.getValue(), message.receiver()));
            return;
        }

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            helper.setTo(message.receiver());
            helper.setSubject(message.subject());
            helper.setText(message.content(), true);
            mailSender.send(mimeMessage);
        } catch (Exception e) {
            log.warn(NotificationLogMessage.KAFKA_RETRY_FAILED.format(KafkaTopic.SLACK_RETRY_TOPIC.getValue(), message.receiver(), retryCount + 1));
            notificationRetryProducer.sendEmailRetryMessage(message.incrementRetryCount());
        }
    }
}

