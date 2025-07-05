package com.example.Triple_clone.domain.plan.web.dto;

import java.util.List;

public record PlanReadAllResponseDto(
        List<PlanReadResponseDto> plans
) {
}
