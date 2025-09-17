package com.example.Triple_clone.domain.plan.web.dto.plan;

import com.example.Triple_clone.domain.plan.domain.Style;
import com.example.Triple_clone.domain.plan.domain.Partner;
import com.example.Triple_clone.domain.plan.domain.Plan;

import java.util.Date;
import java.util.List;

public record PlanReadResponseDto(
        long planId,
        String place,
        Partner partner,
        List<Style> styles,
        Date startDay,
        Date endDay
) {
    public PlanReadResponseDto(Plan plan) {
        this(
                plan.getId(),
                plan.getPlace(),
                plan.getPartner(),
                plan.getStyles(),
                plan.getStartDay(),
                plan.getEndDay()
        );
    }
}
