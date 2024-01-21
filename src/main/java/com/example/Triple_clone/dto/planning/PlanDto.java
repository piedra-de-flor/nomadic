package com.example.Triple_clone.dto.planning;

import com.example.Triple_clone.domain.entity.Plan;

import java.util.List;

public record PlanDto(
        String startDay,
        String endDay,
        String partner,
        String style,
        List<Plan> plans
) {
}
