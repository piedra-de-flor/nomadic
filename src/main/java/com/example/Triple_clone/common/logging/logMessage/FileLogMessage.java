package com.example.Triple_clone.common.logging.logMessage;

public enum FileLogMessage {
    FILE_SEARCH_ERROR("❌ File 검색 중 오류 발생: %s"),
    FILE_HANDLE_ERROR("❌ File 처리 중 오류 발생: %s"),
    IO_ERROR("❌ File IO 오류 발생: %s");

    private final String template;

    FileLogMessage(String template) {
        this.template = template;
    }

    public String format(Object... args) {
        return String.format(template, args);
    }
}
