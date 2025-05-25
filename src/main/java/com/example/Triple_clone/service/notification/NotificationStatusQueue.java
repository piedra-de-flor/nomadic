package com.example.Triple_clone.service.notification;

import com.example.Triple_clone.dto.notification.NotificationSentEvent;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
public class NotificationStatusQueue {
    private final Queue<NotificationSentEvent> queue = new ConcurrentLinkedQueue<>();

    public void enqueue(NotificationSentEvent event) {
        queue.add(event);
    }

    public List<NotificationSentEvent> drainAll() {
        List<NotificationSentEvent> drained = new ArrayList<>();
        NotificationSentEvent event;
        while ((event = queue.poll()) != null) {
            drained.add(event);
        }
        return drained;
    }
}

