package com.example.Triple_clone.common.logging.logMessage;

public enum ReportLogMessage {
    REPORT_SEARCH_FAILED("⚠️ 신고 정보 조회 실패 - reportId: %s"),
    DUPLICATED_REPORT("⚠️ 중복된 신고 - %s: targetId = %s / reporterId = %s");

    private final String template;

    ReportLogMessage(String template) {
        this.template = template;
    }

    public String format(Object... args) {
        return String.format(template, args);
    }
}
