package com.example.Triple_clone.domain.plan.web.dto.plan;

import com.example.Triple_clone.domain.plan.web.dto.plan.PlanReadResponseDto;

import java.util.List;

public record PlanReadAllResponseDto(
        List<PlanReadResponseDto> plans
) {
}
