package com.example.Triple_clone.domain.plan.domain;

import lombok.Getter;

@Getter
public enum TargetType {
    DETAIL_PLAN("DetailPlan"),
    MEMBER("Member"),
    PLAN_STYLE("PlanStyle"),
    PLAN_PARTNER("PlanPartner"),
    PLAN("Plan"),
    PLAN_SHARE("PlanShare");

    private final String value;

    TargetType(String value) {
        this.value = value;
    }
}