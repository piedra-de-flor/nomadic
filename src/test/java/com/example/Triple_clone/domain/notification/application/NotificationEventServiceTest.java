package com.example.Triple_clone.domain.notification.application;

import com.example.Triple_clone.domain.notification.domain.NotificationTarget;
import com.example.Triple_clone.domain.notification.domain.NotificationType;
import com.example.Triple_clone.domain.notification.web.dto.NotificationDto;
import com.example.Triple_clone.domain.notification.application.NotificationEventService;
import com.example.Triple_clone.domain.notification.infra.NotificationSender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.mockito.Mockito.*;

class NotificationEventServiceTest {

    @Mock
    private NotificationSender sender1;

    @Mock
    private NotificationSender sender2;

    private NotificationEventService notificationEventService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        notificationEventService = new NotificationEventService(List.of(sender1, sender2));
    }

    @Test
    void notify_callsSend_onSupportingSender() {
        NotificationDto event = new NotificationDto(NotificationType.REPORT_ALERT, NotificationTarget.PERSONAL, "payload");

        when(sender1.supports(NotificationType.REPORT_ALERT)).thenReturn(true);
        when(sender2.supports(NotificationType.REPORT_ALERT)).thenReturn(false);

        notificationEventService.notify(event);

        verify(sender1, times(1)).process(event);
        verify(sender2, never()).process(any());
    }
}
