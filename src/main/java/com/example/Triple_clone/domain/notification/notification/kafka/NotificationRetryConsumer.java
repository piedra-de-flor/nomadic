package com.example.Triple_clone.domain.notification.notification.kafka;

import com.example.Triple_clone.common.LogMessage;
import com.example.Triple_clone.domain.notification.notification.NotificationMessage;
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
            log.warn(LogMessage.KAFKA_MESSAGE_RETRY_FAIL.format(message));
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
            log.warn("Email 재전송 실패, 재시도 카운트 증가 후 Kafka에 다시 전송: {}", retryCount + 1);
            notificationRetryProducer.sendEmailRetryMessage(message.incrementRetryCount());
        }
    }

    @KafkaListener(topics = "slack-retry-topic", groupId = "slack-retry-group")
    public void retrySendSlack(NotificationMessage message) {
        int retryCount = message.getRetryCount();

        if (retryCount >= MAX_RETRY_COUNT) {
            notificationDlqProducer.sendSlackToDlq(message);
            log.warn("Slack DLQ 전송: 최대 재시도 초과 -> {}", message.receiver());
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
            log.warn("Slack 재전송 실패, 재시도 카운트 증가 후 Kafka에 다시 전송: {}", retryCount + 1);
            notificationRetryProducer.sendEmailRetryMessage(message.incrementRetryCount());
        }
    }
}

