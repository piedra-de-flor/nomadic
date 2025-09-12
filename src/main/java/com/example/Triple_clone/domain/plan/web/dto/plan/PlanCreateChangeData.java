package com.example.Triple_clone.domain.plan.web.dto.plan;

import lombok.Builder;
import java.util.Date;
import java.util.List;

@Builder
public record PlanCreateChangeData(
        String place,
        String partner,
        List<String> styles,
        Date startDay,
        Date endDay
) {}