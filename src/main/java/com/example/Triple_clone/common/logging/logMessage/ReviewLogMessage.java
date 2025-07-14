package com.example.Triple_clone.common.logging.logMessage;

public enum ReviewLogMessage {
    RE_REVIEW_DEPTH_LIMIT("⚠️ 대댓글 작성 실패 - depth 2 초과: parentReviewId = %s"),
    REVIEW_SEARCH_FAILED("⚠️ 리뷰 정보 조회 실패 - reviewId: %s");
    private final String template;

    ReviewLogMessage(String template) {
        this.template = template;
    }

    public String format(Object... args) {
        return String.format(template, args);
    }
}
