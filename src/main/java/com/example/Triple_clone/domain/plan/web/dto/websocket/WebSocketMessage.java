package com.example.Triple_clone.domain.plan.web.dto.websocket;

import java.util.Map;

public record WebSocketMessage(
        WebSocketMessageType type,
        Object data,
        Long detailPlanId,
        String userId,
        String userName,
        long timestamp
) {
    public WebSocketMessage(WebSocketMessageType type, Object data) {
        this(type, data, null, null, null, System.currentTimeMillis());
    }

    public static WebSocketMessage createUserMessage(WebSocketMessageType type, Object data, String userId, String userName) {
        return new WebSocketMessage(type, data, null, userId, userName, System.currentTimeMillis());
    }

    public static WebSocketMessage createPlanMessage(WebSocketMessageType type, Object data, Long detailPlanId, String userId, String userName) {
        return new WebSocketMessage(type, data, detailPlanId, userId, userName, System.currentTimeMillis());
    }

    public static WebSocketMessage error(String errorMessage, String userId, String userName) {
        return createUserMessage(WebSocketMessageType.ERROR,
                Map.of("message", errorMessage), userId, userName);
    }
}