package com.example.Triple_clone.service.notification;

import com.example.Triple_clone.domain.entity.AdminNotificationSetting;
import com.example.Triple_clone.domain.entity.Report;
import com.example.Triple_clone.domain.entity.ReportCount;
import com.example.Triple_clone.domain.vo.NotificationChannelType;
import com.example.Triple_clone.domain.vo.NotificationType;
import com.example.Triple_clone.dto.notification.NotificationDto;
import com.example.Triple_clone.dto.notification.NotificationMessage;
import com.example.Triple_clone.repository.AdminNotificationSettingRepository;
import com.example.Triple_clone.repository.ReportCountRepository;
import com.example.Triple_clone.service.notification.channel.ChannelNotificationSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ReportNotificationSender implements NotificationSender {

    private final AdminNotificationSettingRepository settingRepository;
    private final ReportCountRepository reportCountRepository;
    private final List<ChannelNotificationSender> channelSenders;

    @Override
    public boolean supports(NotificationType type) {
        return type == NotificationType.REPORT_ALERT;
    }

    @Override
    public void send(NotificationDto event) {
        Report report = (Report) event.payload();

        ReportCount reportCount = reportCountRepository.findByTargetIdAndTargetType(
                report.getTargetId(), report.getTargetType().name()
        ).orElse(null);

        long currentCount = reportCount != null ? reportCount.getCount() : 1L;

        List<AdminNotificationSetting> settings = settingRepository.findAll();

        for (AdminNotificationSetting setting : settings) {
            boolean shouldSend = setting.isNotifyEveryReport() || currentCount >= setting.getThresholdCount();
            if (!shouldSend) continue;

            NotificationMessage message = new NotificationMessage(
                    setting.getAdmin().getName(),
                    "[신고 알림] 신고 ID: " + report.getId(),
                    "대상: " + report.getTargetType() + " (ID: " + report.getTargetId() + ")\n"
                            + "사유: " + report.getReason() + "\n"
                            + "내용: " + report.getDetail(),
                    Map.of("reportId", report.getId(), "reportCount", currentCount)
            );

            for (NotificationChannelType channelType : setting.getChannel().getTypes()) {
                channelSenders.stream()
                        .filter(sender -> sender.supports(channelType))
                        .findFirst()
                        .ifPresent(sender -> sender.send(message));
            }
        }
    }
}
