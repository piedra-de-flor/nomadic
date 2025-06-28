package com.example.Triple_clone.service.notification;

import com.example.Triple_clone.domain.member.Member;
import com.example.Triple_clone.domain.notification.Notification;
import com.example.Triple_clone.domain.notification.NotificationTarget;
import com.example.Triple_clone.domain.notification.NotificationType;
import com.example.Triple_clone.domain.notification.notification.NotificationSaveRequest;
import com.example.Triple_clone.domain.notification.notification.NotificationSaveService;
import com.example.Triple_clone.domain.notification.notification.NotificationSentEvent;
import com.example.Triple_clone.domain.member.MemberRepository;
import com.example.Triple_clone.domain.notification.NotificationRepository;
import com.example.Triple_clone.domain.notification.notification.NotificationStatusQueue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import static org.mockito.Mockito.*;

class NotificationSaveServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private NotificationStatusQueue queue;

    @InjectMocks
    private NotificationSaveService notificationSaveService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void 모든_회원에게_알림을_저장하고_큐에_전송한다() {
        NotificationSaveRequest request = new NotificationSaveRequest(
                NotificationType.USER_MESSAGE,
                NotificationTarget.GLOBAL,
                "공지사항",
                "전체 대상 알림입니다.",
                null
        );

        Member mockMember1 = mock(Member.class);
        Member mockMember2 = mock(Member.class);
        when(mockMember1.getId()).thenReturn(1L);
        when(mockMember2.getId()).thenReturn(2L);
        when(memberRepository.findAllByRolesIn(List.of("USER"))).thenReturn(List.of(mockMember1, mockMember2));

        Notification mockNotification = mock(Notification.class);
        when(mockNotification.getId()).thenReturn(100L);
        when(notificationRepository.save(any())).thenReturn(mockNotification);

        notificationSaveService.save(request);

        verify(notificationRepository, times(1)).save(any());
        verify(queue).enqueue(new NotificationSentEvent(100L, 1L));
        verify(queue).enqueue(new NotificationSentEvent(100L, 2L));
    }

    @Test
    void 특정_회원에게_알림을_저장하고_큐에_전송한다() {
        List<Long> userIds = List.of(10L, 20L);

        NotificationSaveRequest request = new NotificationSaveRequest(
                NotificationType.REPORT_ALERT,
                NotificationTarget.PERSONAL,
                "긴급 공지",
                "특정 대상 알림입니다.",
                userIds
        );

        Notification mockNotification1 = mock(Notification.class);
        Notification mockNotification2 = mock(Notification.class);
        when(mockNotification1.getId()).thenReturn(200L);
        when(mockNotification2.getId()).thenReturn(201L);

        when(notificationRepository.save(any()))
                .thenReturn(mockNotification1)
                .thenReturn(mockNotification2);

        notificationSaveService.save(request);

        verify(notificationRepository, times(2)).save(any());
        verify(queue).enqueue(new NotificationSentEvent(200L, 10L));
        verify(queue).enqueue(new NotificationSentEvent(201L, 20L));
    }
}
