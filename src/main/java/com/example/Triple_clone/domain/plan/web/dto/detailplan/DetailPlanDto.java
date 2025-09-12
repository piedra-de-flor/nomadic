package com.example.Triple_clone.domain.plan.web.dto.detailplan;

import com.example.Triple_clone.domain.plan.domain.Location;
import com.example.Triple_clone.domain.plan.domain.Place;
import com.example.Triple_clone.domain.plan.domain.Plan;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.Date;

public record DetailPlanDto(
        long planId,
        @NotNull(message = "Location can not be null")
        Location location,
        @NotNull(message = "Date can not be null")
        Date date,
        String time,
        Long version
) {
    public Place toEntity(Plan plan) {
        return new Place(plan,
                location,
                date,
                time);
    }
}
