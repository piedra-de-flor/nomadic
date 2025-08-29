package com.example.Triple_clone.domain.plan.web.dto.websocket;

public record EditingStateMessage(
        Long detailPlanId,
        String userId,
        String userName,
        boolean isEditing,
        long timestamp
) {
    public static EditingStateMessage createStart(Long detailPlanId, String userId, String userName, String editingField) {
        return new EditingStateMessage(detailPlanId, userId, userName, true, System.currentTimeMillis());
    }

    public static EditingStateMessage createStop(Long detailPlanId, String userId, String userName, String editingField) {
        return new EditingStateMessage(detailPlanId, userId, userName, false, System.currentTimeMillis());
    }
}