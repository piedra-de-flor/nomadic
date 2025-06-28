package com.example.Triple_clone.domain.report;

import com.example.Triple_clone.domain.notification.NotificationTarget;
import com.example.Triple_clone.domain.notification.NotificationType;
import com.example.Triple_clone.domain.notification.notification.NotificationDto;
import com.example.Triple_clone.domain.report.report.ReportCreatedEvent;
import com.example.Triple_clone.domain.notification.notification.NotificationEventService;
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
