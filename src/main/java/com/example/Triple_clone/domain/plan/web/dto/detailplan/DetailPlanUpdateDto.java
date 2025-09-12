package com.example.Triple_clone.domain.plan.web.dto.detailplan;

import com.example.Triple_clone.domain.plan.domain.Location;

import java.util.Date;

public record DetailPlanUpdateDto(
        long planId,
        long detailPlanId,
        Location location,
        Date date,
        String time
) {
}
