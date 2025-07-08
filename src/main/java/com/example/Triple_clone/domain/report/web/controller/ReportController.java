package com.example.Triple_clone.domain.report.web.controller;

import com.example.Triple_clone.common.auth.MemberEmailAspect;
import com.example.Triple_clone.domain.report.application.ReportService;
import com.example.Triple_clone.domain.report.web.dto.ReportRequestDto;
import com.example.Triple_clone.domain.report.web.dto.ReportResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @Operation(summary = "리뷰 신고하기", description = "특정 리뷰를 신고합니다.")
    @ApiResponse(responseCode = "200", description = "성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 형식입니다")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류 발생")
    @ApiResponse(responseCode = "401", description = "권한 인증 오류 발생")
    @PostMapping("/report/{reviewId}")
    public ResponseEntity<ReportResponseDto> reportReview(
            @PathVariable Long reviewId,
            @RequestBody ReportRequestDto request,
            @MemberEmailAspect String email
    ) {
        ReportResponseDto response = reportService.reportReview(reviewId, email, request.reason(), request.detail());
        return ResponseEntity.ok(response);
    }
}
