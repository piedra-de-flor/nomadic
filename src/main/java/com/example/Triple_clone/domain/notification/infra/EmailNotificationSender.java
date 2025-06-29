package com.example.Triple_clone.domain.notification.infra;

import com.example.Triple_clone.common.logging.LogMessage;
import com.example.Triple_clone.common.error.EmailSendFailureException;
import com.example.Triple_clone.domain.notification.domain.NotificationChannelType;
import com.example.Triple_clone.domain.notification.web.dto.NotificationMessage;
import com.example.Triple_clone.domain.notification.web.controller.NotificationRetryProducer;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailNotificationSender implements ChannelNotificationSender {
    private final JavaMailSender mailSender;
    private final NotificationRetryProducer notificationRetryProducer;

    @Override
    public boolean supports(NotificationChannelType channel) {
        return channel == NotificationChannelType.EMAIL;
    }

    @Async("mdcAsyncExecutor")
    @Override
    public void send(NotificationMessage message) {
        try {
            mailSender.send(makeMessage(message));
        } catch (MessagingException e) {
            notificationRetryProducer.sendEmailRetryMessage(message);
            log.warn(LogMessage.EMAIL_SEND_FAIL.format(e.getMessage()));
            log.info(LogMessage.KAFKA_MESSAGE_SEND.format(message.subject()));
            throw new EmailSendFailureException("이메일 전송에 실패했습니다.", e);
        }
    }

    @Override
    public MimeMessage makeMessage(NotificationMessage message) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "UTF-8");

        helper.setTo(message.receiver());
        helper.setSubject(message.subject());
        helper.setText(message.content(), true);

        return mimeMessage;
    }

    @Override
    public NotificationChannelType getChannelType() {
        return NotificationChannelType.EMAIL;
    }
}
