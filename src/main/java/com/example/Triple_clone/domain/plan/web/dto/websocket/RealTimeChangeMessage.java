package com.example.Triple_clone.domain.plan.web.dto.websocket;

public record RealTimeChangeMessage(
        Long detailPlanId,
        String field,
        Object oldValue,
        Object newValue,
        String userId,
        String userName,
        String changeType,
        long timestamp
) {
    public RealTimeChangeMessage withUserInfo(String userId, String userName) {
        return new RealTimeChangeMessage(detailPlanId, field, oldValue, newValue,
                userId, userName, changeType, System.currentTimeMillis());
    }
}
