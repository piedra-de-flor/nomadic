package com.example.Triple_clone.domain.report.application;

import com.example.Triple_clone.common.logging.logMessage.ReportLogMessage;
import com.example.Triple_clone.domain.report.domain.Report;
import com.example.Triple_clone.domain.report.domain.ReportTargetType;
import com.example.Triple_clone.domain.report.infra.ReportAdminRepository;
import com.example.Triple_clone.domain.report.infra.ReportCountQueryRepository;
import com.example.Triple_clone.domain.report.infra.ReportRepository;
import com.example.Triple_clone.domain.report.web.dto.ReportCountDto;
import com.example.Triple_clone.domain.report.web.dto.ReportResponseDto;
import com.example.Triple_clone.domain.report.web.dto.ReportSearchDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportAdminService {
    private final ReportAdminRepository reportAdminRepository;
    private final ReportRepository reportRepository;
    private final ReportCountQueryRepository reportCountQueryRepository;

    public Page<ReportResponseDto> getReports(ReportSearchDto condition, Pageable pageable) {
        return reportAdminRepository.searchReports(condition, pageable);
    }

    public List<ReportCountDto> getReportCountsByCondition(ReportTargetType targetType, Long minReportCount, Pageable pageable) {
        Page<ReportCountDto> reportCounts = reportCountQueryRepository.searchReportCounts(targetType, minReportCount, pageable);

        return reportCounts.stream()
                .collect(Collectors.toList());
    }

    public Page<ReportResponseDto> getReportsByTarget(ReportTargetType targetType, Long targetId, Pageable pageable) {
        return reportAdminRepository.searchReportsByTarget(targetType, targetId, pageable);
    }


    @Transactional
    public void approveReport(Long reportId) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> {
                    log.warn(ReportLogMessage.REPORT_SEARCH_FAILED.format(reportId));
                    return new EntityNotFoundException("신고를 찾을 수 없습니다.");
                });

        report.approve();
    }

    @Transactional
    public void rejectReport(Long reportId) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> {
                    log.warn(ReportLogMessage.REPORT_SEARCH_FAILED.format(reportId));
                    return new EntityNotFoundException("신고를 찾을 수 없습니다.");
                });

        report.reject();
    }
}
