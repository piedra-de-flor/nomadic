package com.example.Triple_clone.common.logging.logMessage;

public enum RecommendLogMessage {
    RECOMMEND_SEARCH_FAILED("⚠️ %s - 존재하지 않는 추천 장소: %s");

    private final String template;

    RecommendLogMessage(String template) {
        this.template = template;
    }

    public String format(Object... args) {
        return String.format(template, args);
    }
}
