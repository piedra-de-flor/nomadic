package com.example.Triple_clone.web.controller.report;

import com.example.Triple_clone.domain.vo.ReportTargetType;
import com.example.Triple_clone.dto.report.ReportCountDto;
import com.example.Triple_clone.dto.report.ReportResponseDto;
import com.example.Triple_clone.dto.report.ReportSearchDto;
import com.example.Triple_clone.service.report.ReportAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/reports")
public class ReportAdminController {
    private final ReportAdminService reportService;

    @GetMapping
    public Page<ReportResponseDto> searchReports(
            ReportSearchDto condition,
            @PageableDefault(size = 10) Pageable pageable) {
        return reportService.getReports(condition, pageable);
    }

    @GetMapping("/reports/count/{targetType}/{minReportCount}")
    public List<ReportCountDto> getReportCountsByCondition(
            @PathVariable(required = false) String targetType,
            @PathVariable(required = false) Long minReportCount,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        return reportService.getReportCountsByCondition(ReportTargetType.valueOf(targetType), minReportCount, pageable);
    }

    @GetMapping("/reports/{targetType}/{targetId}")
    public Page<ReportResponseDto> getReportsByTarget(
            @PathVariable String targetType,
            @PathVariable Long targetId,
            Pageable pageable
    ) {
        return reportService.getReportsByTarget(ReportTargetType.valueOf(targetType), targetId, pageable);
    }

    @PostMapping("/{reportId}/approve")
    public ResponseEntity<Void> approveReport(@PathVariable Long reportId) {
        reportService.approveReport(reportId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{reportId}/reject")
    public ResponseEntity<Void> rejectReport(@PathVariable Long reportId) {
        reportService.rejectReport(reportId);
        return ResponseEntity.ok().build();
    }
}
