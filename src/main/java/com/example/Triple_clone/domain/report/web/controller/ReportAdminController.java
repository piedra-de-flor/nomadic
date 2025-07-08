package com.example.Triple_clone.domain.report.web.controller;

import com.example.Triple_clone.domain.report.application.ReportAdminService;
import com.example.Triple_clone.domain.report.domain.ReportTargetType;
import com.example.Triple_clone.domain.report.web.dto.ReportCountDto;
import com.example.Triple_clone.domain.report.web.dto.ReportResponseDto;
import com.example.Triple_clone.domain.report.web.dto.ReportSearchDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    @Operation(summary = "(관리자용) 신고 조회", description = "사용자들의 신고를 확인합니다.")
    @ApiResponse(responseCode = "200", description = "성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 형식입니다")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류 발생")
    @ApiResponse(responseCode = "401", description = "권한 인증 오류 발생")
    @GetMapping
    public Page<ReportResponseDto> searchReports(
            ReportSearchDto condition,
            @PageableDefault(size = 10) Pageable pageable) {
        return reportService.getReports(condition, pageable);
    }

    @Operation(summary = "(관리자용) 누적 신고수 조건 조회", description = "신고 누적 수에 따라 신고를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 형식입니다")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류 발생")
    @ApiResponse(responseCode = "401", description = "권한 인증 오류 발생")
    @GetMapping("/reports/count/{targetType}/{minReportCount}")
    public List<ReportCountDto> getReportCountsByCondition(
            @PathVariable(required = false) String targetType,
            @PathVariable(required = false) Long minReportCount,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        return reportService.getReportCountsByCondition(ReportTargetType.valueOf(targetType), minReportCount, pageable);
    }

    @Operation(summary = "(관리자용) 신고 조건 조회", description = "사용자들의 신고를 조건에 맞게 조회합니다.")
    @ApiResponse(responseCode = "200", description = "성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 형식입니다")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류 발생")
    @ApiResponse(responseCode = "401", description = "권한 인증 오류 발생")
    @GetMapping("/reports/{targetType}/{targetId}")
    public Page<ReportResponseDto> getReportsByTarget(
            @PathVariable String targetType,
            @PathVariable Long targetId,
            Pageable pageable
    ) {
        return reportService.getReportsByTarget(ReportTargetType.valueOf(targetType), targetId, pageable);
    }

    @Operation(summary = "(관리자용) 신고 승인", description = "특정 신고를 승인합니다.")
    @ApiResponse(responseCode = "200", description = "성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 형식입니다")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류 발생")
    @ApiResponse(responseCode = "401", description = "권한 인증 오류 발생")
    @PostMapping("/{reportId}/approve")
    public ResponseEntity<Void> approveReport(@PathVariable Long reportId) {
        reportService.approveReport(reportId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "(관리자용) 신고 기각", description = "특정 신고를 기각합니다.")
    @ApiResponse(responseCode = "200", description = "성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 형식입니다")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류 발생")
    @ApiResponse(responseCode = "401", description = "권한 인증 오류 발생")
    @PostMapping("/{reportId}/reject")
    public ResponseEntity<Void> rejectReport(@PathVariable Long reportId) {
        reportService.rejectReport(reportId);
        return ResponseEntity.ok().build();
    }
}
