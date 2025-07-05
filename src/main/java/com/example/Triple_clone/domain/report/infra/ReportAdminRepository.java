package com.example.Triple_clone.domain.report.infra;

import com.example.Triple_clone.domain.report.web.dto.ReportResponseDto;
import com.example.Triple_clone.domain.report.web.dto.ReportSearchDto;
import com.example.Triple_clone.domain.report.domain.ReportTargetType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReportAdminRepository {
    Page<ReportResponseDto> searchReports(ReportSearchDto condition, Pageable pageable);
    Page<ReportResponseDto> searchReportsByTarget(ReportTargetType targetType, Long targetId, Pageable pageable);
}
