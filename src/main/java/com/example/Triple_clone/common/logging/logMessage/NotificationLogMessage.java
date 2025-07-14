package com.example.Triple_clone.common.logging.logMessage;

public enum NotificationLogMessage {
    NOTIFICATION_READ_FAILED("⚠️ 알림 읽음 표시 실패 - 존재하지 않는 알림: %s"),
    UNSUPPORTED_CHANNEL("⚠️ %s 알림 생성 실패 - 지원하지 않는 알림 채널: %s"),
    EMAIL_SEND_FAILED("⚠️ 이메일 전송 실패 - %s"),
    SLACK_SEND_FAILED("⚠️ 슬랙 전송 실패 - %s"),
    KAFKA_PRODUCE_FAILED("❌ kafka %s event 전송 실패 (%s) - %s"),
    KAFKA_RETRY_FAILED("⚠️ kafka %s event 재시도 실패 (%s) - count 증가 후 다시 재시도: 3/%s"),
    KAFKA_EVENT_TO_DLQ("✅ kafka %s event 최대 재시도 횟수 초과 - DLQ로 전송: %s"),
    KAFKA_EVENT_TO_DLQ_FAIL("❌ kafka %s event DLQ 전송 실패 - DLQ로 전송: %s"),
    KAFKA_PRODUCED("✅ kafka %s event 전송 - %s");


    private final String template;

    NotificationLogMessage(String template) {
        this.template = template;
    }

    public String format(Object... args) {
        return String.format(template, args);
    }
}
