package com.example.Triple_clone.dto.planning;

public record PlanPartnerUpdateDto(
        long userId,
        long planId,
        String partner
) {
}
