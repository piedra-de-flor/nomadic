package com.example.Triple_clone.dto.planning;

import com.example.Triple_clone.domain.entity.DetailPlan;
import com.example.Triple_clone.domain.entity.Plan;

import java.util.Date;

public record DetailPlanDto(
        long planId,
        String name,
        String location,
        Date date,
        String time
) {
    public DetailPlan toEntity(Plan plan) {
        return DetailPlan.builder()
                .plan(plan)
                .name(name)
                .location(location)
                .date(date)
                .time(time)
                .build();
    }
}
