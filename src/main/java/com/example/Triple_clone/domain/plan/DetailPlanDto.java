package com.example.Triple_clone.domain.plan;

import jakarta.validation.constraints.NotNull;

import java.util.Date;

public record DetailPlanDto(
        long planId,
        @NotNull(message = "Location can not be null")
        Location location,
        @NotNull(message = "Date can not be null")
        Date date,
        String time
) {
    public Place toEntity(Plan plan) {
        return new Place(plan,
                location,
                date,
                time);
    }
}
