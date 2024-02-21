package com.example.Triple_clone.dto.planning;

import com.example.Triple_clone.domain.vo.Location;

import java.util.Date;

public record DetailPlanUpdateDto(
        long planId,
        long detailPlanId,
        Location location,
        Date date,
        String time
) {
}
