package com.example.Triple_clone.common.logging.logMessage;

public enum AuthLogMessage {
    AUTH_FAILED("⚠️ 회원 정보 권한 인증 실패 - 사용자: %s"),
    TOKEN_EXPIRED("⚠️ 토큰 만료 - userId: %s"),
    TOKEN_HAS_NO_AUTH("⚠️ 접근 권한 에러 - 토큰 권한 정보 누락: %s"),
    UNSUPPORTED_TOKEN("⚠️ JWT 토큰 검증 실패 - 지원하지 않는 토큰: %s"),
    TOKEN_IS_NOT_VALID("⚠️ JWT 토큰 검증 실패 - 바르지 못한 토큰값: %s"),
    TOKEN_ERROR("⚠️ JWT 토큰 검증 실패 - 예상치 못한 에러 : %s");

    private final String template;

    AuthLogMessage(String template) {
        this.template = template;
    }

    public String format(Object... args) {
        return String.format(template, args);
    }
}
