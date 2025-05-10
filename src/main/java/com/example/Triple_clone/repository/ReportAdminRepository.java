package com.example.Triple_clone.repository;

import com.example.Triple_clone.dto.report.ReportResponseDto;
import com.example.Triple_clone.dto.report.ReportSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReportAdminRepository {
    Page<ReportResponseDto> searchReports(ReportSearchDto condition, Pageable pageable);
}
