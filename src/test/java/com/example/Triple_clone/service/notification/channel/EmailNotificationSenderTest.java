package com.example.Triple_clone.service.notification.channel;

import com.example.Triple_clone.domain.vo.NotificationChannelType;
import com.example.Triple_clone.dto.notification.NotificationMessage;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailNotificationSenderTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailNotificationSender emailNotificationSender;

    @BeforeEach
    void setUp() {
        mailSender = mock(JavaMailSender.class);
        emailNotificationSender = new EmailNotificationSender(mailSender);
    }

    @Test
    void supports_shouldReturnTrue_forEmailChannel() {
        boolean result = emailNotificationSender.supports(NotificationChannelType.EMAIL);
        assertThat(result).isTrue();
    }

    @Test
    void supports_shouldReturnFalse_forOtherChannels() {
        assertThat(emailNotificationSender.supports(NotificationChannelType.SLACK)).isFalse();
        assertThat(emailNotificationSender.supports(NotificationChannelType.BOTH)).isFalse();
    }

    @Test
    void send_shouldCallMailSender_withCorrectMessage() {
        NotificationMessage message = new NotificationMessage(
                "test@example.com",
                "Test Subject",
                "Test Content",
                null
        );

        MimeMessage mimeMessage = new MimeMessage((Session) null);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        emailNotificationSender.send(message);

        verify(mailSender, times(1)).send(mimeMessage);
    }
}
