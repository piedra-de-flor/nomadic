package com.example.Triple_clone.service.notification.channel;

import com.example.Triple_clone.domain.vo.NotificationChannelType;
import com.example.Triple_clone.dto.notification.NotificationMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailNotificationSender implements ChannelNotificationSender {
    private final JavaMailSender mailSender;

    @Override
    public boolean supports(NotificationChannelType channel) {
        return channel == NotificationChannelType.EMAIL;
    }

    @Override
    public void send(NotificationMessage message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(message.receiver());
        mailMessage.setSubject(message.subject());
        mailMessage.setText(message.content());

        mailSender.send(mailMessage);
    }
}
