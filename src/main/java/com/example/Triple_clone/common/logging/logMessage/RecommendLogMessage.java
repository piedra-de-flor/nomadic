package com.example.Triple_clone.common.logging.logMessage;

public enum RecommendLogMessage {
    RECOMMEND_SEARCH_FAILED("⚠️ %s - 존재하지 않는 추천 장소: %s"),
    RECOMMEND_PLACE_COMMAND_AUTH_FAILED("⚠️ 추천 장소는 관리자만 포스팅할 수 있습니다."),
    RECOMMEND_AUTH_FAILED("⚠️ 해당 포스트의 소유자가 아닙니다: author= %s");

    private final String template;

    RecommendLogMessage(String template) {
        this.template = template;
    }

    public String format(Object... args) {
        return String.format(template, args);
    }
}
