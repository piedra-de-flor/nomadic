package com.example.Triple_clone.domain.plan.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.socket.WebSocketSession;

@Getter
@AllArgsConstructor
public class EditingSession {
    private final String userId;
    private final String userName;
    private final Long detailPlanId;
    private final String field;
    private final long startTime;
    private final WebSocketSession session;

    public boolean isExpired(long timeoutMs) {
        return System.currentTimeMillis() - startTime > timeoutMs;
    }

    public String getEditingKey(Long planId) {
        return planId + ":" + detailPlanId + ":" + field;
    }
}
