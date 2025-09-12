package com.example.Triple_clone.domain.plan.web.dto.websocket;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum WebSocketMessageType {
    PLAN_JOINED("PLAN_JOINED", "계획 참여"),
    USER_JOINED("USER_JOINED", "사용자 참여"),
    USER_LEFT("USER_LEFT", "사용자 퇴장"),
    START_EDIT("START_EDIT", "편집 시작"),
    EDIT_STARTED("EDIT_STARTED", "편집 시작 알림"),
    EDIT_CANCELLED("EDIT_CANCELLED", "편집 취소"),
    REAL_TIME_CHANGE("REAL_TIME_CHANGE", "실시간 변경"),
    UPDATE_DETAIL_PLAN("UPDATE_DETAIL_PLAN", "계획 업데이트 요청"),
    DETAIL_PLAN_UPDATED("DETAIL_PLAN_UPDATED", "계획 업데이트 완료"),
    CANCEL_EDIT("CANCEL_EDIT", "편집 취소 요청"),
    ERROR("ERROR", "에러");

    private final String value;
    private final String description;

    public static WebSocketMessageType fromValue(String value) {
        for (WebSocketMessageType type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown WebSocket message type: " + value);
    }

    public boolean isClientRequest() {
        return this == START_EDIT ||
                this == UPDATE_DETAIL_PLAN ||
                this == CANCEL_EDIT ||
                this == REAL_TIME_CHANGE;
    }

    public boolean isServerResponse() {
        return this == PLAN_JOINED ||
                this == USER_JOINED ||
                this == USER_LEFT ||
                this == EDIT_STARTED ||
                this == EDIT_CANCELLED ||
                this == DETAIL_PLAN_UPDATED ||
                this == ERROR;
    }
}