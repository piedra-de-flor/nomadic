package com.example.Triple_clone.domain.plan.web.dto;

import com.example.Triple_clone.domain.plan.domain.Style;
import com.example.Triple_clone.domain.plan.domain.Partner;
import com.example.Triple_clone.domain.plan.domain.Plan;

import java.util.Date;
import java.util.List;

public record PlanReadResponseDto(
        String place,
        Partner partner,
        List<Style> styles,
        Date startDay,
        Date endDay
) {
    public PlanReadResponseDto(Plan plan) {
        this(
                plan.getPlace(),
                plan.getPartner(),
                plan.getStyles(),
                plan.getStartDay(),
                plan.getEndDay()
        );
    }
}
