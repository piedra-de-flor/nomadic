package com.example.Triple_clone.domain.plan.web.dto.plan;

import lombok.Builder;

@Builder
public record PlanPartnerChangeData(
        String oldPartner,
        String newPartner
) {}