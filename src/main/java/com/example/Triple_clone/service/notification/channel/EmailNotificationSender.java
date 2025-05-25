package com.example.Triple_clone.service.notification.channel;

import com.example.Triple_clone.domain.vo.NotificationChannelType;
import com.example.Triple_clone.dto.notification.NotificationMessage;
import com.example.Triple_clone.web.exception.EmailSendFailureException;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailNotificationSender implements ChannelNotificationSender {
    private final JavaMailSender mailSender;

    @Override
    public boolean supports(NotificationChannelType channel) {
        return channel == NotificationChannelType.EMAIL;
    }

    @Async
    @Override
    public void send(NotificationMessage message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(message.receiver());
        mailMessage.setSubject(message.subject());
        mailMessage.setText(message.content());

        try {
            mailSender.send(mailMessage);
        } catch (MailException e) {
            throw new EmailSendFailureException("이메일 전송에 실패했습니다.", e);
        }
    }

    @Override
    public NotificationChannelType getChannelType() {
        return NotificationChannelType.EMAIL;
    }
}
