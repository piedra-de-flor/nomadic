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
import com.example.Triple_clone.service.notification.channel.EmailNotificationSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ReportNotificationSender implements NotificationSender {

    private final AdminNotificationSettingRepository settingRepository;
    private final List<ChannelNotificationSender> channelSenders;

    @Override
    public boolean supports(NotificationType type) {
        return type == NotificationType.REPORT_ALERT;
    }

    @Override
    public void send(NotificationDto dto) {
        ReportCreatedEvent event = (ReportCreatedEvent) dto.payload();
        Report report = event.report();
        long count = event.reportCount();

        List<AdminNotificationSetting> settings = settingRepository.findAll();

        for (AdminNotificationSetting setting : settings) {
            boolean shouldNotify = setting.isNotifyEveryReport() || count >= setting.getThresholdCount();
            if (!shouldNotify) continue;

            NotificationMessage message = new NotificationMessage(
                    setting.getAdmin().getEmail(),
                    NotificationSubject.REPORT.getValue(),
                    NotificationContentTemplate.REPORT.format(
                            report.getTargetType(), report.getTargetId(), report.getReason()),
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
