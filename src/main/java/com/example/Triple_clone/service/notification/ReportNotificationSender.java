package com.example.Triple_clone.service.notification;

import com.example.Triple_clone.domain.entity.AdminNotificationSetting;
import com.example.Triple_clone.domain.entity.Report;
import com.example.Triple_clone.domain.vo.*;
import com.example.Triple_clone.dto.notification.NotificationDto;
import com.example.Triple_clone.dto.notification.NotificationMessage;
import com.example.Triple_clone.dto.notification.NotificationSaveRequest;
import com.example.Triple_clone.dto.report.ReportCreatedEvent;
import com.example.Triple_clone.repository.AdminNotificationSettingRepository;
import com.example.Triple_clone.service.notification.channel.ChannelNotificationSender;
import com.example.Triple_clone.web.support.HtmlTemplateRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
