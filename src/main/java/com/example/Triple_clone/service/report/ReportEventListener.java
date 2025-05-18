package com.example.Triple_clone.service.report;

import com.example.Triple_clone.domain.vo.NotificationType;
import com.example.Triple_clone.dto.notification.NotificationDto;
import com.example.Triple_clone.dto.report.ReportCreatedEvent;
import com.example.Triple_clone.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReportEventListener {

    private final NotificationService notificationService;

    @EventListener
    public void handleReportCreated(ReportCreatedEvent event) {
        notificationService.notify(new NotificationDto(
                NotificationType.REPORT_ALERT,
                event
        ));
    }
}
