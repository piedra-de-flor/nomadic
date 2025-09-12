package com.example.Triple_clone.domain.plan.web.dto.detailplan;

import com.example.Triple_clone.domain.plan.domain.DetailPlan;
import com.example.Triple_clone.domain.plan.domain.Location;

import java.util.Date;

public record DetailPlanData(
        Long id,
        Location location,
        Date date,
        String time,
        Long version
) {
    public static DetailPlanData from(DetailPlan detailPlan) {
        return new DetailPlanData(
                detailPlan.getId(),
                detailPlan.getLocation(),
                detailPlan.getDate(),
                detailPlan.getTime(),
                detailPlan.getVersion()
        );
    }
}
