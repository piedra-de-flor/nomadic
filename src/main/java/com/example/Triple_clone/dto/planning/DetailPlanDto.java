package com.example.Triple_clone.dto.planning;

import com.example.Triple_clone.domain.entity.DetailPlan;
import com.example.Triple_clone.domain.entity.Plan;
import com.example.Triple_clone.domain.vo.Location;
import jakarta.validation.constraints.NotBlank;

import java.util.Date;

public record DetailPlanDto(
        long planId,
        @NotBlank(message = "Location can not be null")
        Location location,
        @NotBlank(message = "Date can not be null")
        Date date,
        String time
) {
    public DetailPlan toEntity(Plan plan) {
        return DetailPlan.builder()
                .plan(plan)
                .location(location)
                .date(date)
                .time(time)
                .build();
    }
}
