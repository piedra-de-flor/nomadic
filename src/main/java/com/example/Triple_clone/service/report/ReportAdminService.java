package com.example.Triple_clone.service.report;

import com.example.Triple_clone.domain.entity.Report;
import com.example.Triple_clone.domain.entity.ReportCount;
import com.example.Triple_clone.domain.vo.ReportTargetType;
import com.example.Triple_clone.dto.report.ReportCountDto;
import com.example.Triple_clone.dto.report.ReportResponseDto;
import com.example.Triple_clone.dto.report.ReportSearchDto;
import com.example.Triple_clone.repository.ReportAdminRepository;
import com.example.Triple_clone.repository.ReportCountQueryRepository;
import com.example.Triple_clone.repository.ReportCountRepository;
import com.example.Triple_clone.repository.ReportRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
                .orElseThrow(() -> new EntityNotFoundException("신고를 찾을 수 없습니다."));

        report.approve();
    }

    @Transactional
    public void rejectReport(Long reportId) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new EntityNotFoundException("신고를 찾을 수 없습니다."));

        report.reject();
    }
}
