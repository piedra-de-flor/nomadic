package com.example.Triple_clone.domain.report.web.dto;

import com.example.Triple_clone.domain.report.domain.ReportingReason;
import jakarta.validation.constraints.NotNull;

public record ReportRequestDto(
        @NotNull ReportingReason reason,
        String detail
) {}
