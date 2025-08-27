package com.example.Triple_clone.domain.notification.infra;

import com.example.Triple_clone.common.template.HtmlTemplateRenderer;
import com.example.Triple_clone.domain.notification.application.NotificationSaveService;
import com.example.Triple_clone.domain.notification.domain.NotificationTarget;
import com.example.Triple_clone.domain.notification.domain.NotificationType;
import com.example.Triple_clone.domain.notification.web.dto.NotificationDto;
import com.example.Triple_clone.domain.notification.web.dto.NotificationSaveRequest;
import com.example.Triple_clone.domain.plan.web.dto.notification.PlanNotificationEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class PlanNotificationSender extends NotificationSender {
    private static final Map<String, String> ACTION_TITLES = Map.of(
            "SHARED", "[계획 공유] 새로운 계획이 공유되었습니다",
            "ACCEPTED", "[계획 공유] 공유 요청이 수락되었습니다",
            "REJECTED", "[계획 공유] 공유 요청이 거절되었습니다"
    );

    public PlanNotificationSender(HtmlTemplateRenderer htmlTemplateRenderer,
                                  List<ChannelNotificationSender> channelSenders,
                                  NotificationSaveService notificationSaveService) {
        super(htmlTemplateRenderer, channelSenders, notificationSaveService);
    }

    @Override
    public boolean supports(NotificationType type) {
        return type == NotificationType.USER_MESSAGE;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public NotificationSaveRequest prepareAndSend(NotificationDto dto) {
        PlanNotificationEvent event = (PlanNotificationEvent) dto.payload();

        log.info("Processing plan notification - Plan: {}, Action: {}, Actor: {}, Receiver: {}",
                event.getPlan().getId(),
                event.getAction(),
                event.getActor().getEmail(),
                event.getReceiver().getEmail());

        String content = event.generateMessage();
        String title = ACTION_TITLES.getOrDefault(event.getAction(), "[계획 알림]");

        return new NotificationSaveRequest(
                NotificationType.USER_MESSAGE,
                NotificationTarget.PERSONAL,
                title,
                content,
                List.of(event.getReceiver().getId())
        );
    }
}
