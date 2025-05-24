package com.example.Triple_clone.service.notification.channel;

import com.example.Triple_clone.domain.vo.NotificationChannelType;
import com.example.Triple_clone.dto.notification.NotificationMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class EmailNotificationSenderTest {

    private JavaMailSender mailSender;
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

        emailNotificationSender.send(message);

        ArgumentCaptor<SimpleMailMessage> mailMessageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender, times(1)).send(mailMessageCaptor.capture());

        SimpleMailMessage sentMessage = mailMessageCaptor.getValue();
        assertThat(sentMessage.getTo()).containsExactly("test@example.com");
        assertThat(sentMessage.getSubject()).isEqualTo("Test Subject");
        assertThat(sentMessage.getText()).isEqualTo("Test Content");
    }
}
