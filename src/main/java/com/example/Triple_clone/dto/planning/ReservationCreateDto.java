package com.example.Triple_clone.dto.planning;

import com.example.Triple_clone.domain.entity.DetailPlan;
import com.example.Triple_clone.domain.entity.Plan;
import com.example.Triple_clone.domain.entity.Reservation;
import com.example.Triple_clone.domain.vo.Location;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

public record ReservationCreateDto(
        long planId,
        @NotNull(message = "Location can not be null")
        Location location,
        @NotNull(message = "Date can not be null")
        Date date,
        String time
){
        public DetailPlan toEntity(Plan plan) {
                return new Reservation(plan,
                        location,
                        date,
                        time);
        }
}
