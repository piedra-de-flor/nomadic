package com.example.Triple_clone.service.notification.channel;

import com.example.Triple_clone.domain.vo.NotificationChannelType;
import com.example.Triple_clone.dto.notification.NotificationMessage;
import com.example.Triple_clone.service.notification.kafka.NotificationRetryProducer;
import com.example.Triple_clone.web.exception.EmailSendFailureException;
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
            log.warn("❌ 이메일 전송 실패: {}, Kafka로 메시지 전송", e.getMessage());
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
