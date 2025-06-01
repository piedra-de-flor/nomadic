package com.example.Triple_clone.service.notification.kafka;

import com.example.Triple_clone.dto.notification.NotificationMessage;
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
public class EmailRetryConsumer {
    private final JavaMailSender mailSender;
    private final EmailRetryProducer emailRetryProducer;
    private final EmailDlqProducer emailDlqProducer;

    private static final int MAX_RETRY_COUNT = 3;

    @KafkaListener(topics = "email-retry-topic", groupId = "email-retry-group")
    public void retrySend(NotificationMessage message) {
        int retryCount = message.getRetryCount();

        if (retryCount >= MAX_RETRY_COUNT) {
            emailDlqProducer.sendToDlq(message);
            log.warn("DLQ 전송: 최대 재시도 초과 -> {}", message.receiver());
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
            log.warn("재전송 실패, 재시도 카운트 증가 후 Kafka에 다시 전송: {}", retryCount + 1);
            emailRetryProducer.sendRetryMessage(message.incrementRetryCount());
        }
    }
}

