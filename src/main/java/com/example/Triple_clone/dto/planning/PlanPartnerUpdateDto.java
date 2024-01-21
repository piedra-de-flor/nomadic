package com.example.Triple_clone.dto.planning;

public record PlanPartnerUpdateDto(
        PlanDto planDto,
        long userId,
        long planId,
        String partner
) {
}
