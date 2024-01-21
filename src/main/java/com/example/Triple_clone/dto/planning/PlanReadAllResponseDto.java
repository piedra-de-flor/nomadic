package com.example.Triple_clone.dto.planning;

import com.example.Triple_clone.domain.entity.Plan;

import java.util.List;

public record PlanReadAllResponseDto(
        List<Plan> plans
) {
}
