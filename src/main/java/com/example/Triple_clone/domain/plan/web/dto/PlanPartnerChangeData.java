package com.example.Triple_clone.domain.plan.web.dto;

import lombok.Builder;

@Builder
public record PlanPartnerChangeData(
        String oldPartner,
        String newPartner
) {}