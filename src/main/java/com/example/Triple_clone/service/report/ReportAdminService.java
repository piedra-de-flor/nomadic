package com.example.Triple_clone.service.report;

import com.example.Triple_clone.domain.entity.Report;
import com.example.Triple_clone.dto.report.ReportResponseDto;
import com.example.Triple_clone.dto.report.ReportSearchDto;
import com.example.Triple_clone.repository.ReportAdminRepository;
import com.example.Triple_clone.repository.ReportRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportAdminService {
    private final ReportAdminRepository reportAdminRepository;
    private final ReportRepository reportRepository;

    public Page<ReportResponseDto> getReports(ReportSearchDto condition, Pageable pageable) {
        return reportAdminRepository.searchReports(condition, pageable);
    }

    public void approveReport(Long reportId) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new EntityNotFoundException("신고를 찾을 수 없습니다."));

        report.approve();
    }

    public void rejectReport(Long reportId) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new EntityNotFoundException("신고를 찾을 수 없습니다."));

        report.reject();
    }
}
