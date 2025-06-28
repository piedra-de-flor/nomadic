package com.example.Triple_clone.domain.plan;

import java.util.Date;

public record DetailPlanUpdateDto(
        long planId,
        long detailPlanId,
        Location location,
        Date date,
        String time
) {
}
