package com.example.Triple_clone.common.logging.logMessage;

public enum ESLogMessage {
    ES_SEARCH_ERROR("❌ Elasticsearch 검색 중 오류 발생: %s");

    private final String template;

    ESLogMessage(String template) {
        this.template = template;
    }

    public String format(Object... args) {
        return String.format(template, args);
    }
}
