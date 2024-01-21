package com.example.Triple_clone.dto.planning;

import java.util.List;

public record PlanStyleUpdateDto(
        long userId,
        long planId,
        List<String> styles
){
}
