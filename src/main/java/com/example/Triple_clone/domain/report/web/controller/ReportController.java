package com.example.Triple_clone.domain.report.web.controller;

import com.example.Triple_clone.common.auth.MemberEmailAspect;
import com.example.Triple_clone.domain.report.application.ReportService;
import com.example.Triple_clone.domain.report.web.dto.ReportRequestDto;
import com.example.Triple_clone.domain.report.web.dto.ReportResponseDto;
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
