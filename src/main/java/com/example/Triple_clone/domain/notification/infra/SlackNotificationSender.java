package com.example.Triple_clone.domain.notification.infra;

import com.example.Triple_clone.common.logging.LogMessage;
import com.example.Triple_clone.common.error.SlackSendFailureException;
import com.example.Triple_clone.domain.notification.domain.NotificationChannelType;
import com.example.Triple_clone.domain.notification.web.dto.NotificationMessage;
import com.example.Triple_clone.domain.notification.web.controller.NotificationRetryProducer;
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

    @Async("mdcAsyncExecutor")
    @Override
    public void send(NotificationMessage message) {
        try {
            slackClient.sendMessage(message);
        } catch (Exception e) {
            slackRetryProducer.sendSlackRetryMessage(message);
            log.warn(LogMessage.SLACK_SEND_FAIL.format(e.getMessage()));
            log.info(LogMessage.KAFKA_MESSAGE_SEND.format(message.receiver()));
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
