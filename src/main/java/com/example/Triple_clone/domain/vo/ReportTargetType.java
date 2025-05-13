package com.example.Triple_clone.domain.vo;

public enum ReportTargetType {
    REVIEW,
    COMMENT,
    POST;

    public static ReportTargetType from(String value) {
        try {
            return ReportTargetType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("지원하지 않는 신고 대상 타입입니다: " + value);
        }
    }
}
