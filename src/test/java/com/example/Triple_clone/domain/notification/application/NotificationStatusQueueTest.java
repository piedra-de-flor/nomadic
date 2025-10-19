package com.example.Triple_clone.domain.notification.application;

import com.example.Triple_clone.domain.notification.web.dto.NotificationSentEvent;
import com.example.Triple_clone.domain.notification.infra.NotificationStatusQueue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class NotificationStatusQueueTest {

    private NotificationStatusQueue queue;

    @BeforeEach
    void setUp() {
        queue = new NotificationStatusQueue();
    }

    @Test
    void 큐에_이벤트_정상_삽입() {
        NotificationSentEvent event = new NotificationSentEvent(1L, 10L);

        queue.enqueue(event);
        List<NotificationSentEvent> drained = queue.drainAll();

        assertThat(drained).hasSize(1);
        assertThat(drained.get(0)).isEqualTo(event);
    }

    @Test
    void 큐에_있는_모든_이벤드들_가져오기() {
        NotificationSentEvent e1 = new NotificationSentEvent(1L, 10L);
        NotificationSentEvent e2 = new NotificationSentEvent(2L, 20L);
        NotificationSentEvent e3 = new NotificationSentEvent(3L, 30L);

        queue.enqueue(e1);
        queue.enqueue(e2);
        queue.enqueue(e3);

        List<NotificationSentEvent> drained = queue.drainAll();

        assertThat(drained).containsExactly(e1, e2, e3);

        List<NotificationSentEvent> afterDrain = queue.drainAll();
        assertThat(afterDrain).isEmpty();
    }

    @Test
    void 큐가_비어있을때_큐_가져와보기() {
        NotificationSentEvent event = new NotificationSentEvent(1L, 10L);
        queue.enqueue(event);

        List<NotificationSentEvent> firstDrain = queue.drainAll();
        List<NotificationSentEvent> secondDrain = queue.drainAll();

        assertThat(firstDrain).hasSize(1);
        assertThat(secondDrain).isEmpty();
    }
}
