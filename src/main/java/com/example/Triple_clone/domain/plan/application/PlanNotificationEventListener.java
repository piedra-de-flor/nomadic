package com.example.Triple_clone.domain.plan.application;

import com.example.Triple_clone.domain.notification.application.NotificationEventService;
import com.example.Triple_clone.domain.notification.domain.NotificationTarget;
import com.example.Triple_clone.domain.notification.domain.NotificationType;
import com.example.Triple_clone.domain.notification.web.dto.NotificationDto;
import com.example.Triple_clone.domain.plan.web.dto.notification.PlanShareAcceptedNotificationEvent;
import com.example.Triple_clone.domain.plan.web.dto.notification.PlanShareRejectedNotificationEvent;
import com.example.Triple_clone.domain.plan.web.dto.notification.PlanSharedNotificationEvent;
import com.example.Triple_clone.domain.plan.web.dto.planshare.event.PlanSharedEvent;
import com.example.Triple_clone.domain.plan.web.dto.planshare.event.ShareAcceptedEvent;
import com.example.Triple_clone.domain.plan.web.dto.planshare.event.ShareRejectedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class PlanNotificationEventListener {
    private final NotificationEventService notificationEventService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handlePlanSharedEvent(PlanSharedEvent event) {
        log.info("Converting PlanSharedEvent to notification event for plan: {}", event.getPlan().getId());

        PlanSharedNotificationEvent notificationEvent = new PlanSharedNotificationEvent(
                event.getPlan(),
                event.getChangedBy(),
                event.getSharedMember(),
                event.getRole()
        );

        NotificationDto notificationDto = new NotificationDto(
                NotificationType.USER_MESSAGE,
                NotificationTarget.PERSONAL,
                notificationEvent
        );

        notificationEventService.notify(notificationDto);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleShareAcceptedEvent(ShareAcceptedEvent event) {
        log.info("Converting ShareAcceptedEvent to notification event for plan: {}", event.getPlan().getId());

        PlanShareAcceptedNotificationEvent notificationEvent = new PlanShareAcceptedNotificationEvent(
                event.getPlan(),
                event.getChangedBy(),
                event.getPlan().getMember()
        );

        NotificationDto notificationDto = new NotificationDto(
                NotificationType.USER_MESSAGE,
                NotificationTarget.PERSONAL,
                notificationEvent
        );

        notificationEventService.notify(notificationDto);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleShareRejectedEvent(ShareRejectedEvent event) {
        log.info("Converting ShareRejectedEvent to notification event for plan: {}", event.getPlan().getId());

        PlanShareRejectedNotificationEvent notificationEvent = new PlanShareRejectedNotificationEvent(
                event.getPlan(),
                event.getChangedBy(),
                event.getPlan().getMember()
        );

        NotificationDto notificationDto = new NotificationDto(
                NotificationType.USER_MESSAGE,
                NotificationTarget.PERSONAL,
                notificationEvent
        );

        notificationEventService.notify(notificationDto);
    }
}