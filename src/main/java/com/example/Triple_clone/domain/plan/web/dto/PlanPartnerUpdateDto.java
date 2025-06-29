package com.example.Triple_clone.domain.plan.web.dto;

public record PlanPartnerUpdateDto(
        PlanDto planDto,
        String partner
) {
}
