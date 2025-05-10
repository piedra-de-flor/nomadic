package com.example.Triple_clone.dto.report;

import com.example.Triple_clone.domain.vo.ReportingReason;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ReportRequestDto {
    @NotNull
    private Long reporterId;

    @NotNull
    private ReportingReason reason;

    private String detail;
}