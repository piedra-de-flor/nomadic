package com.example.Triple_clone.dto.planning;

import java.util.List;

public record PlanReadAllResponseDto(
        List<PlanReadResponseDto> plans
) {
}
