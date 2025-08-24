package com.example.Triple_clone.domain.plan.web.dto.detailplan;

import com.example.Triple_clone.domain.plan.domain.Location;
import lombok.Builder;

import java.util.Date;

@Builder
public record DetailPlanUpdateChangeData(
        Location oldLocation,
        Location newLocation,
        Date oldDate,
        Date newDate,
        String oldTime,
        String newTime
) {}