package com.example.Triple_clone.domain.report.report;

import com.example.Triple_clone.domain.report.ReportingReason;
import jakarta.validation.constraints.NotNull;

public record ReportRequestDto(
        @NotNull ReportingReason reason,
        String detail
) {}
