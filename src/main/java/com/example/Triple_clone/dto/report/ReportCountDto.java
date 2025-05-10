package com.example.Triple_clone.dto.report;

public class ReportCountDto {
    private Long targetId;
    private String targetType;
    private Long reportCount;

    public ReportCountDto(Long targetId, String targetType, Long reportCount) {
        this.targetId = targetId;
        this.targetType = targetType;
        this.reportCount = reportCount;
    }
}
