package com.example.Triple_clone.service.notification;

import com.example.Triple_clone.domain.entity.AdminNotificationSetting;
import com.example.Triple_clone.domain.entity.Report;
import com.example.Triple_clone.domain.vo.NotificationChannelType;
import com.example.Triple_clone.domain.vo.NotificationContentTemplate;
import com.example.Triple_clone.domain.vo.NotificationSubject;
import com.example.Triple_clone.domain.vo.NotificationType;
import com.example.Triple_clone.dto.notification.NotificationDto;
import com.example.Triple_clone.dto.notification.NotificationMessage;
import com.example.Triple_clone.dto.report.ReportCreatedEvent;
import com.example.Triple_clone.repository.AdminNotificationSettingRepository;
import com.example.Triple_clone.service.notification.channel.ChannelNotificationSender;
import com.example.Triple_clone.web.support.HtmlTemplateRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Component
public class ReportNotificationSender extends NotificationSender {
    private final AdminNotificationSettingRepository settingRepository;

    @Autowired
    public ReportNotificationSender(HtmlTemplateRenderer htmlTemplateRenderer, List<ChannelNotificationSender> channelSenders, AdminNotificationSettingRepository settingRepository) {
        super(htmlTemplateRenderer, channelSenders);
        this.settingRepository = settingRepository;
    }

    @Override
    public boolean supports(NotificationType type) {
        return type == NotificationType.REPORT_ALERT;
    }

    @Transactional
    @Override
    public void send(NotificationDto dto) {
        ReportCreatedEvent event = (ReportCreatedEvent) dto.payload();
        Report report = event.report();
        long count = event.reportCount();

        List<AdminNotificationSetting> settings = settingRepository.findAll();

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
                NotificationChannelType type = resolveChannelType(sender);
                if (setting.getChannel().includes(type)) {
                    sender.send(message);
                } else {
                    throw new IllegalArgumentException("지원하지 않는 채널 타입입니다.");
                }
            }
        }
    }

    public NotificationChannelType resolveChannelType(ChannelNotificationSender sender) {
        return sender.getChannelType();
    }
}
