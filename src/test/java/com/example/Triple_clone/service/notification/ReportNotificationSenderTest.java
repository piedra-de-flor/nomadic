package com.example.Triple_clone.service.notification;

import com.example.Triple_clone.domain.entity.AdminNotificationSetting;
import com.example.Triple_clone.domain.entity.Member;
import com.example.Triple_clone.domain.entity.Report;
import com.example.Triple_clone.domain.vo.*;
import com.example.Triple_clone.dto.notification.NotificationDto;
import com.example.Triple_clone.dto.notification.NotificationMessage;
import com.example.Triple_clone.dto.report.ReportCreatedEvent;
import com.example.Triple_clone.repository.AdminNotificationSettingRepository;
import com.example.Triple_clone.service.notification.channel.ChannelNotificationSender;
import com.example.Triple_clone.service.notification.channel.EmailNotificationSender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class ReportNotificationSenderTest {

    @Mock
    private AdminNotificationSettingRepository settingRepository;

    @Mock
    private EmailNotificationSender emailSender;

    @InjectMocks
    private ReportNotificationSender reportNotificationSender;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        reportNotificationSender = new ReportNotificationSender(settingRepository, List.of(emailSender));
    }

    @Test
    void 지원하는_알림타입이_REPORT_ALERT이면_true를_반환한다() {
        boolean result = reportNotificationSender.supports(NotificationType.REPORT_ALERT);
        assertThat(result).isTrue();
    }

    @Test
    void 모든_신고에_대해_알림설정이_되어있으면_알림을_전송한다() {
        Member admin = mock(Member.class);
        when(admin.getEmail()).thenReturn("admin@example.com");

        NotificationChannel channel = new NotificationChannel(Set.of(NotificationChannelType.EMAIL));

        AdminNotificationSetting setting = mock(AdminNotificationSetting.class);
        when(setting.isNotifyEveryReport()).thenReturn(true);
        when(setting.getThresholdCount()).thenReturn(5);
        when(setting.getAdmin()).thenReturn(admin);
        when(setting.getChannel()).thenReturn(channel);
        when(emailSender.getChannelType()).thenReturn(NotificationChannelType.EMAIL);

        when(settingRepository.findAll()).thenReturn(List.of(setting));

        Report report = mock(Report.class);
        when(report.getTargetType()).thenReturn(ReportTargetType.REVIEW);
        when(report.getTargetId()).thenReturn(123L);
        when(report.getReason()).thenReturn(ReportingReason.INAPPROPRIATE);
        when(report.getId()).thenReturn(99L);

        ReportCreatedEvent event = mock(ReportCreatedEvent.class);
        when(event.report()).thenReturn(report);
        when(event.reportCount()).thenReturn(3L);

        NotificationDto dto = new NotificationDto(NotificationType.REPORT_ALERT, event);

        reportNotificationSender.send(dto);

        verify(emailSender).send(any(NotificationMessage.class));
    }

    @Test
    void 신고수_임계치를_초과하면_알림을_전송한다() {
        Member admin = mock(Member.class);
        when(admin.getEmail()).thenReturn("admin@example.com");

        NotificationChannel channel = new NotificationChannel(Set.of(NotificationChannelType.EMAIL));

        AdminNotificationSetting setting = mock(AdminNotificationSetting.class);
        when(setting.isNotifyEveryReport()).thenReturn(false);
        when(setting.getThresholdCount()).thenReturn(5);
        when(setting.getAdmin()).thenReturn(admin);
        when(setting.getChannel()).thenReturn(channel);
        when(emailSender.getChannelType()).thenReturn(NotificationChannelType.EMAIL);

        when(settingRepository.findAll()).thenReturn(List.of(setting));

        Report report = mock(Report.class);
        when(report.getTargetType()).thenReturn(ReportTargetType.REVIEW);
        when(report.getTargetId()).thenReturn(123L);
        when(report.getReason()).thenReturn(ReportingReason.INAPPROPRIATE);
        when(report.getId()).thenReturn(99L);

        ReportCreatedEvent event = mock(ReportCreatedEvent.class);
        when(event.report()).thenReturn(report);
        when(event.reportCount()).thenReturn(10L);

        NotificationDto dto = new NotificationDto(NotificationType.REPORT_ALERT, event);

        reportNotificationSender.send(dto);

        verify(emailSender).send(any(NotificationMessage.class));
    }

    @Test
    void 임계치_이하이고_모든_신고알림도_끄면_알림을_전송하지_않는다() {
        Member admin = mock(Member.class);
        when(admin.getEmail()).thenReturn("admin@example.com");

        NotificationChannel channel = new NotificationChannel(Set.of(NotificationChannelType.EMAIL));

        AdminNotificationSetting setting = mock(AdminNotificationSetting.class);
        when(setting.isNotifyEveryReport()).thenReturn(false);
        when(setting.getThresholdCount()).thenReturn(5);
        when(setting.getAdmin()).thenReturn(admin);
        when(setting.getChannel()).thenReturn(channel);

        when(settingRepository.findAll()).thenReturn(List.of(setting));

        Report report = mock(Report.class);
        when(report.getTargetType()).thenReturn(ReportTargetType.REVIEW);
        when(report.getTargetId()).thenReturn(123L);
        when(report.getReason()).thenReturn(ReportingReason.INAPPROPRIATE);
        when(report.getId()).thenReturn(99L);

        ReportCreatedEvent event = mock(ReportCreatedEvent.class);
        when(event.report()).thenReturn(report);
        when(event.reportCount()).thenReturn(3L);

        NotificationDto dto = new NotificationDto(NotificationType.REPORT_ALERT, event);

        reportNotificationSender.send(dto);

        verifyNoInteractions(emailSender);
    }

    @Test
    void 지원하지_않는_채널타입이면_예외를_던진다() {
        Member admin = mock(Member.class);
        when(admin.getEmail()).thenReturn("admin@example.com");

        NotificationChannel channel = new NotificationChannel(Set.of(NotificationChannelType.EMAIL));

        AdminNotificationSetting setting = mock(AdminNotificationSetting.class);
        when(setting.isNotifyEveryReport()).thenReturn(true);
        when(setting.getThresholdCount()).thenReturn(5);
        when(setting.getAdmin()).thenReturn(admin);
        when(setting.getChannel()).thenReturn(channel);
        when(emailSender.getChannelType()).thenReturn(NotificationChannelType.SLACK);

        when(settingRepository.findAll()).thenReturn(List.of(setting));

        Report report = mock(Report.class);
        when(report.getTargetType()).thenReturn(ReportTargetType.REVIEW);
        when(report.getTargetId()).thenReturn(123L);
        when(report.getReason()).thenReturn(ReportingReason.INAPPROPRIATE);
        when(report.getId()).thenReturn(99L);

        ReportCreatedEvent event = mock(ReportCreatedEvent.class);
        when(event.report()).thenReturn(report);
        when(event.reportCount()).thenReturn(3L);

        NotificationDto dto = new NotificationDto(NotificationType.REPORT_ALERT, event);

        assertThatThrownBy(() -> reportNotificationSender.send(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("지원하지 않는 채널 타입입니다");
    }
}
