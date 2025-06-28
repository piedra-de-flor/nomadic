package com.example.Triple_clone.domain.notification.notification;

import com.example.Triple_clone.domain.notification.notification.NotificationSentEvent;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class NotificationStatusQueue {
    private final AtomicReference<Queue<NotificationSentEvent>> queue =
            new AtomicReference<>(new ConcurrentLinkedQueue<>());


    public void enqueue(NotificationSentEvent event) {
        queue.get().add(event);
    }

    public List<NotificationSentEvent> drainAll() {
        Queue<NotificationSentEvent> oldQueue = queue.getAndSet(new ConcurrentLinkedQueue<>());
        List<NotificationSentEvent> drained = new ArrayList<>();
        NotificationSentEvent event;

        while ((event = oldQueue.poll()) != null) {
            drained.add(event);
        }

        return drained;
    }
}

