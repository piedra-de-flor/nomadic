package com.example.Triple_clone.common.logging.logMessage;

public enum PlanLogMessage {
    PLAN_DOESNT_HAVE_THE_DETAIL_PLAN("⚠️ %s - 목표 계획내 포함되지 않는 세부 계획: planId = %s / detailPlanId = %s"),
    PLAN_SEARCH_FAILED("⚠️ 계획 정보 조회 실패 - planId: %s"),
    DETAIL_PLAN_SEARCH_FAILED("⚠️ 세부 계획 정보 조회 실패 - detailPlanId: %s"),
    PLAN_ACCESS_FAILED("⚠️ 계획 소유자가 아닙니다 - email: %s / planId: %s");

    private final String template;

    PlanLogMessage(String template) {
        this.template = template;
    }

    public String format(Object... args) {
        return String.format(template, args);
    }
}
