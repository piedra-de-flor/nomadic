package com.example.Triple_clone.dto.planning;

import java.util.List;

public record PlanStyleUpdateDto(
        PlanDto planDto,
        List<String> styles
){
}
