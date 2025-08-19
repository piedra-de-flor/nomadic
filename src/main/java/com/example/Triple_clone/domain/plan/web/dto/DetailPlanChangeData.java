package com.example.Triple_clone.domain.plan.web.dto;

import com.example.Triple_clone.domain.plan.domain.Location;
import lombok.Builder;

import java.util.Date;

@Builder
public record DetailPlanChangeData(
        Location location,
        Date date,
        String time
) {}