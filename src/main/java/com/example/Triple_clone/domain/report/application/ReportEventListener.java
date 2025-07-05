package com.example.Triple_clone.domain.report.application;

import com.example.Triple_clone.domain.notification.domain.NotificationTarget;
import com.example.Triple_clone.domain.notification.domain.NotificationType;
import com.example.Triple_clone.domain.notification.web.dto.NotificationDto;
import com.example.Triple_clone.domain.notification.application.NotificationEventService;
import com.example.Triple_clone.domain.report.web.dto.ReportCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class ReportEventListener {

    private final NotificationEventService notificationEventService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @EventListener
    public void handleReportCreated(ReportCreatedEvent event) {
        notificationEventService.notify(new NotificationDto(
                NotificationType.REPORT_ALERT,
                NotificationTarget.ADMIN,
                event
        ));
    }
}
