package com.example.Triple_clone.web.controller.report;

import com.example.Triple_clone.dto.report.ReportRequestDto;
import com.example.Triple_clone.dto.report.ReportResponseDto;
import com.example.Triple_clone.service.report.ReportService;
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

    @PostMapping("/{reviewId}/report")
    public ResponseEntity<ReportResponseDto> reportReview(
            @PathVariable Long reviewId,
            @RequestBody ReportRequestDto request
    ) {
        ReportResponseDto response = reportService.reportReview(reviewId, request.reporterId(), request.reason(), request.detail());
        return ResponseEntity.ok(response);
    }
}
