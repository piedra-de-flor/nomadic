package com.example.Triple_clone.domain.plan.web.dto.websocket;

public record WebSocketMessage(
        String type,
        Object data,
        Long detailPlanId,
        String userId,
        String userName,
        long timestamp
) {
    public WebSocketMessage(String type, Object data) {
        this(type, data, null, null, null, System.currentTimeMillis());
    }

    public static WebSocketMessage createUserMessage(String type, Object data, String userId, String userName) {
        return new WebSocketMessage(type, data, null, userId, userName, System.currentTimeMillis());
    }

    public static WebSocketMessage createPlanMessage(String type, Object data, Long detailPlanId, String userId, String userName) {
        return new WebSocketMessage(type, data, detailPlanId, userId, userName, System.currentTimeMillis());
    }
}