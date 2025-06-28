package com.example.Triple_clone.domain.notification.notification;

import com.example.Triple_clone.domain.notification.AdminNotificationSetting;
import com.example.Triple_clone.domain.report.Report;
import com.example.Triple_clone.domain.notification.*;
import com.example.Triple_clone.domain.report.report.ReportCreatedEvent;
import com.example.Triple_clone.domain.notification.AdminNotificationSettingRepository;
import com.example.Triple_clone.domain.notification.notification.channel.ChannelNotificationSender;
import com.example.Triple_clone.common.HtmlTemplateRenderer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class ReportNotificationSender extends NotificationSender {
    private final AdminNotificationSettingRepository settingRepository;

    public ReportNotificationSender(HtmlTemplateRenderer htmlTemplateRenderer,
                                    List<ChannelNotificationSender> channelSenders,
                                    NotificationSaveService notificationSaveService,
                                    AdminNotificationSettingRepository settingRepository) {
        super(htmlTemplateRenderer, channelSenders, notificationSaveService);
        this.settingRepository = settingRepository;
    }

    @Override
    public boolean supports(NotificationType type) {
        return type == NotificationType.REPORT_ALERT;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public NotificationSaveRequest prepareAndSend(NotificationDto dto) {
        ReportCreatedEvent event = (ReportCreatedEvent) dto.payload();
        Report report = event.report();
        long count = event.reportCount();

        List<AdminNotificationSetting> settings = settingRepository.findAll();
        List<Long> notifiedAdminIds = new ArrayList<>();

        for (AdminNotificationSetting setting : settings) {
            boolean shouldNotify = setting.isNotifyEveryReport() || count >= setting.getThresholdCount();
            if (!shouldNotify) continue;

            String htmlContent = htmlTemplateRenderer.render(NotificationContentTemplate.REPORT.getPath(), Map.of(
                    "reporterEmail", report.getReporter().getEmail(),
                    "targetType", report.getTargetType().name(),
                    "targetId", report.getTargetId(),
                    "reason", report.getReason().name(),
                    "content", report.getDetail() != null ? report.getDetail() : ""
            ));

            NotificationMessage message = new NotificationMessage(
                    setting.getAdmin().getEmail(),
                    NotificationSubject.REPORT.getValue(),
                    htmlContent,
                    Map.of("reportId", report.getId())
            );

            for (ChannelNotificationSender sender : channelSenders) {
                NotificationChannelType channelType = resolveChannelType(sender);
                if (setting.getChannel().includes(channelType)) {
                    sender.send(message);
                } else {
                    log.warn("⚠️ 신고 알림 생성 실패 - 지원하지 않는 채널: {}", channelType);
                    throw new IllegalArgumentException("지원하지 않는 채널 타입입니다.");
                }
            }

            notifiedAdminIds.add(setting.getAdmin().getId());
        }

        return new NotificationSaveRequest(
                NotificationType.REPORT_ALERT,
                NotificationTarget.ADMIN,
                NotificationSubject.REPORT.getValue(),
                report.getDetail(),
                notifiedAdminIds
        );
    }
}
