package com.example.Triple_clone.service.notification;

import com.example.Triple_clone.TestMailConfig;
import com.example.Triple_clone.domain.entity.Member;
import com.example.Triple_clone.domain.vo.NotificationTarget;
import com.example.Triple_clone.domain.vo.NotificationType;
import com.example.Triple_clone.dto.notification.NotificationSaveRequest;
import com.example.Triple_clone.repository.MemberRepository;
import com.example.Triple_clone.service.notification.kafka.NotificationRetryConsumer;
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
class NotificationSaveServicePerformanceTest {
    @MockBean
    private NotificationRetryConsumer notificationRetryConsumer;

    @MockBean
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private NotificationSaveService notificationSaveService;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        for (int i = 0; i < 100_000; i++) {
            Member member = Member.builder()
                    .email("user" + i + "@test.com")
                    .roles(List.of("USER"))
                    .build();
            memberRepository.save(member);
        }
    }

    @Test
    void GLOBAL_알림_저장_성능_테스트() {
        NotificationSaveRequest request = new NotificationSaveRequest(
                NotificationType.USER_MESSAGE,
                NotificationTarget.GLOBAL,
                "대상 전체 알림",
                "모든 유저에게 발송됩니다.",
                null
        );

        Runtime runtime = Runtime.getRuntime();
        long beforeUsedMem = runtime.totalMemory() - runtime.freeMemory();
        int threadBefore = Thread.activeCount();
        long start = System.currentTimeMillis();

        notificationSaveService.save(request);

        long end = System.currentTimeMillis();
        int threadAfter = Thread.activeCount();
        long afterUsedMem = runtime.totalMemory() - runtime.freeMemory();

        System.out.println("🟢 NotificationSaveService 성능 결과");
        System.out.println("총 소요 시간(ms): " + (end - start));
        System.out.println("메모리 사용량 변화(bytes): " + (afterUsedMem - beforeUsedMem));
        System.out.println("스레드 수 변화: " + (threadAfter - threadBefore));
    }
}
