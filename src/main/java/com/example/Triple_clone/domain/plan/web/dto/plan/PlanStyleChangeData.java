package com.example.Triple_clone.domain.plan.web.dto.plan;

import lombok.Builder;
import java.util.List;

@Builder
public record PlanStyleChangeData(
        List<String> oldStyles,
        List<String> newStyles
) {}