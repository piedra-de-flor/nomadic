package com.example.Triple_clone.common.logging.logMessage;

public enum MemberLogMessage {
    MEMBER_SEARCH_FAILED_BY_EMAIL("⚠️ 회원 정보 email 조회 실패 - 사용자 email: %s"),
    MEMBER_SEARCH_FAILED_BY_ID("⚠️ 회원 정보 id 조회 실패 - 사용자 id: %s"),
    MEMBER_SIGN_UP_FAIL("⚠️ 회원 가입 실패 - 중복된 이메일: %s");

    private final String template;

    MemberLogMessage(String template) {
        this.template = template;
    }

    public String format(Object... args) {
        return String.format(template, args);
    }
}
