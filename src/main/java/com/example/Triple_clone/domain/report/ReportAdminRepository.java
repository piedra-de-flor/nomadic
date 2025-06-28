package com.example.Triple_clone.domain.report;

import com.example.Triple_clone.domain.report.ReportTargetType;
import com.example.Triple_clone.domain.report.report.ReportResponseDto;
import com.example.Triple_clone.domain.report.report.ReportSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReportAdminRepository {
    Page<ReportResponseDto> searchReports(ReportSearchDto condition, Pageable pageable);
    Page<ReportResponseDto> searchReportsByTarget(ReportTargetType targetType, Long targetId, Pageable pageable);
}
