package com.example.Triple_clone.domain.notification.infra;

import com.example.Triple_clone.common.logging.logMessage.NotificationLogMessage;
import com.example.Triple_clone.common.template.HtmlTemplateRenderer;
import com.example.Triple_clone.domain.notification.application.NotificationSaveService;
import com.example.Triple_clone.domain.notification.domain.*;
import com.example.Triple_clone.domain.notification.web.NotificationContentTemplate;
import com.example.Triple_clone.domain.notification.web.dto.NotificationDto;
import com.example.Triple_clone.domain.notification.web.dto.NotificationMessage;
import com.example.Triple_clone.domain.notification.web.dto.NotificationSaveRequest;
import com.example.Triple_clone.domain.report.domain.Report;
import com.example.Triple_clone.domain.report.web.dto.ReportCreatedEvent;
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
                    log.warn(NotificationLogMessage.UNSUPPORTED_CHANNEL.format(report.getClass().getSimpleName(), channelType));
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
