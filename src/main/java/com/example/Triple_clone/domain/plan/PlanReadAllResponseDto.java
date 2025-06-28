package com.example.Triple_clone.domain.plan;

import java.util.List;

public record PlanReadAllResponseDto(
        List<PlanReadResponseDto> plans
) {
}
