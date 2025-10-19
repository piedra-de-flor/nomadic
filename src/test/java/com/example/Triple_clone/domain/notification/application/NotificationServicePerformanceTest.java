package com.example.Triple_clone.domain.notification.application;

import com.example.Triple_clone.configuration.TestMailConfig;
import com.example.Triple_clone.domain.member.domain.Member;
import com.example.Triple_clone.domain.notification.domain.Notification;
import com.example.Triple_clone.domain.notification.domain.NotificationTarget;
import com.example.Triple_clone.domain.notification.domain.NotificationType;
import com.example.Triple_clone.domain.notification.web.dto.NotificationSearchDto;
import com.example.Triple_clone.domain.member.infra.MemberRepository;
import com.example.Triple_clone.domain.notification.infra.NotificationRepository;
import com.example.Triple_clone.domain.member.application.UserService;
import com.example.Triple_clone.domain.notification.web.controller.NotificationRetryConsumer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Import(TestMailConfig.class)
@Transactional
class NotificationServicePerformanceTest {
    @MockBean
    private NotificationRetryConsumer notificationRetryConsumer;

    @MockBean
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private UserService userService;

    private Member testMember;

    @BeforeEach
    void setupUser() {
        testMember = memberRepository.save(Member.builder()
                .email("test_user@test.com")
                .roles(List.of("USER"))
                .build());
    }

    private void generateNotifications(int count) {
        for (int i = 0; i < count; i++) {
            Notification global = Notification.builder()
                    .type(NotificationType.USER_MESSAGE)
                    .target(NotificationTarget.GLOBAL)
                    .title("Global Notification " + i)
                    .content("내용")
                    .build();
            notificationRepository.save(global);

            Notification personal = Notification.builder()
                    .type(NotificationType.REPORT_ALERT)
                    .target(NotificationTarget.PERSONAL)
                    .targetUserId(testMember.getId())
                    .title("Personal Notification " + i)
                    .content("내용")
                    .build();
            notificationRepository.save(personal);
        }
    }

    private void runPerformanceTest(int count) {
        generateNotifications(count);

        Runtime runtime = Runtime.getRuntime();
        long beforeUsedMem = runtime.totalMemory() - runtime.freeMemory();
        int threadBefore = Thread.activeCount();
        long start = System.currentTimeMillis();

        List<NotificationSearchDto> result = notificationService.getUserNotifications(testMember.getEmail());

        long end = System.currentTimeMillis();
        int threadAfter = Thread.activeCount();
        long afterUsedMem = runtime.totalMemory() - runtime.freeMemory();

        System.out.println("====== 알림 개수: " + (count * 2) + "개 ======");
        System.out.println("조회된 알림 개수: " + result.size());
        System.out.println("총 소요 시간(ms): " + (end - start));
        System.out.println("메모리 사용량 변화(bytes): " + (afterUsedMem - beforeUsedMem));
        System.out.println("스레드 수 변화: " + (threadAfter - threadBefore));
        System.out.println();
    }

    @Test
    void getUserNotifications_performanceBySize() {
        List<Integer> testSizes = List.of(10, 100, 1000, 10000);

        for (int size : testSizes) {
            runPerformanceTest(size);
        }
    }
}
