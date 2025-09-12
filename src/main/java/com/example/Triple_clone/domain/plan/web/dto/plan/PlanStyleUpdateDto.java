package com.example.Triple_clone.domain.plan.web.dto.plan;

import com.example.Triple_clone.domain.plan.web.dto.plan.PlanDto;

import java.util.List;

public record PlanStyleUpdateDto(
        PlanDto planDto,
        List<String> styles
){
}
