package com.example.Triple_clone.web.controller.report;

import com.example.Triple_clone.dto.report.ReportRequestDto;
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
    public ResponseEntity<String> reportReview(
            @PathVariable Long reviewId,
            @RequestBody ReportRequestDto request
    ) {
        reportService.reportReview(reviewId, request.getReporterId(), request.getReason(), request.getDetail());
        return ResponseEntity.ok("신고가 접수되었습니다.");
    }
}
