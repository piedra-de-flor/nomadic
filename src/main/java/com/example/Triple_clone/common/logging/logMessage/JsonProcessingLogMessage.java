package com.example.Triple_clone.common.logging.logMessage;

public enum JsonProcessingLogMessage {
    JSON_PROCESSING_LOG_MESSAGE("❌ 변경 데이터 직렬화 실패 - change data: %s / change type: %s");
    private final String template;

    JsonProcessingLogMessage(String template) {
        this.template = template;
    }

    public String format(Object... args) {
        return String.format(template, args);
    }
}
