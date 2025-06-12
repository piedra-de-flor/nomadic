package com.example.Triple_clone.service.notification.channel;

import com.example.Triple_clone.domain.vo.NotificationChannelType;
import com.example.Triple_clone.dto.notification.NotificationMessage;
import com.example.Triple_clone.service.notification.kafka.NotificationRetryProducer;
import com.example.Triple_clone.web.exception.SlackSendFailureException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SlackNotificationSender implements ChannelNotificationSender {

    private final SlackClient slackClient;
    private final NotificationRetryProducer slackRetryProducer;

    @Override
    public boolean supports(NotificationChannelType channel) {
        return channel == NotificationChannelType.SLACK;
    }

    @Async
    @Override
    public void send(NotificationMessage message) {
        try {
            slackClient.sendMessage(message);
        } catch (Exception e) {
            slackRetryProducer.sendSlackRetryMessage(message);
            log.warn("❌ Slack 전송 실패: {}, Kafka로 메시지 전송", e.getMessage());
            throw new SlackSendFailureException("Slack 전송에 실패했습니다.", e);
        }
    }

    @Override
    public MimeMessage makeMessage(NotificationMessage message) {
        throw new UnsupportedOperationException("Slack은 MimeMessage를 사용하지 않습니다.");
    }

    @Override
    public NotificationChannelType getChannelType() {
        return NotificationChannelType.SLACK;
    }
}
