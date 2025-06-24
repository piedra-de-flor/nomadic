package com.example.Triple_clone.domain.vo;

public enum LogMessage {

    USER_NOT_FOUND_ID("⚠️ 사용자 조회 실패 - 존재하지 않는 회원 ID: %s"),
    USER_NOT_FOUND_EMAIL("⚠️ 사용자 조회 실패 - 존재하지 않는 회원 Email: %s"),
    USER_AUTH_FAIL("⚠️ 사용자 인증 실패 - 존재하지 않는 이메일: %s"),
    BATCH_PROCESS_START("✅ 배치 process 시작: %s"),
    BATCH_PROCESS_SUCCESS("✅ 배치 process 성공: %s"),
    BATCH_PROCESS_FAIL("❌ %s 배치 process 실패: %s"),
    EMAIL_SEND_FAIL("❌ Email 전송 실패: %s"),
    SLACK_SEND_FAIL("❌ Slack 전송 실패: %s"),
    KAFKA_MESSAGE_SEND("✅ Kafka로 이벤트 발송: %s"),


    private final String template;

    LogMessage(String template) {
        this.template = template;
    }

    public String format(Object... args) {
        return String.format(template, args);
    }
}
