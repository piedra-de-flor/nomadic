package com.example.Triple_clone.domain.plan;

import java.util.List;

public record PlanStyleUpdateDto(
        PlanDto planDto,
        List<String> styles
){
}
