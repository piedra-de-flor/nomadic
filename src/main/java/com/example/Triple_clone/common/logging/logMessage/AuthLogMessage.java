package com.example.Triple_clone.common.logging.logMessage;

public enum AuthLogMessage {
    LOGIN_FAILED("⚠️ 로그인 실패 - 사용자: {}"),
    TOKEN_EXPIRED("⚠️ 토큰 만료 - userId: {}");

    AuthLogMessage(String s) {
    }
}
