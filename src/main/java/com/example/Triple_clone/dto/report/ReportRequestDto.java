package com.example.Triple_clone.dto.report;

import com.example.Triple_clone.domain.vo.ReportingReason;
import jakarta.validation.constraints.NotNull;

public record ReportRequestDto(
        @NotNull Long reporterId,
        @NotNull ReportingReason reason,
        String detail
) {}
