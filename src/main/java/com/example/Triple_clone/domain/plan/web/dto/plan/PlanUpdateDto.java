package com.example.Triple_clone.domain.plan.web.dto.plan;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

public record PlanUpdateDto(
        PlanDto planDto,
        String name,
        @NotNull Date start,
        @NotNull Date end
) {
    @AssertTrue(message = "start는 end보다 이전이어야 합니다.")
    public boolean isValidDateRange() {
        return start.before(end);
    }
}