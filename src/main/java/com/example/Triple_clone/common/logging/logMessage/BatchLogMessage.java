package com.example.Triple_clone.common.logging.logMessage;

public enum BatchLogMessage {
    BATCH_PROCESS_STARTED("✅ Batch 프로세스 시작: %s"),
    BATCH_PROCESS_ENDED("✅ Batch 프로세스 종료: %s"),
    BATCH_PROCESS_FAILED("❌ Batch 프로세스 실패 - %s: %s");

    private final String template;

    BatchLogMessage(String template) {
        this.template = template;
    }

    public String format(Object... args) {
        return String.format(template, args);
    }
}
