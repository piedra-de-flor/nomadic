package com.example.Triple_clone.domain.plan;

import com.example.Triple_clone.domain.accommodation.Accommodation;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

public record ReservationCreateDto(
        long planId,
        long accommodationId,
        @NotNull(message = "Location can not be null")
        Location location,
        @NotNull(message = "Date can not be null")
        Date date,
        String time
){
        public DetailPlan toEntity(Plan plan, Accommodation accommodation) {
                return new Reservation(plan,
                        location,
                        date,
                        time,
                        accommodation);
        }
}
