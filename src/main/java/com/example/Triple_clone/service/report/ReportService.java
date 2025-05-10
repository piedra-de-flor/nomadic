package com.example.Triple_clone.service.report;

import com.example.Triple_clone.domain.entity.Member;
import com.example.Triple_clone.domain.entity.Review;
import com.example.Triple_clone.domain.entity.Report;
import com.example.Triple_clone.domain.vo.ReportingReason;
import com.example.Triple_clone.repository.MemberRepository;
import com.example.Triple_clone.repository.ReportRepository;
import com.example.Triple_clone.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final MemberRepository memberRepository;
    private final ReviewRepository reviewRepository;

    public void reportReview(Long reviewId, Long reporterId, ReportingReason reason, String detail) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("리뷰가 존재하지 않습니다."));

        Member reporter = memberRepository.findById(reporterId)
                .orElseThrow(() -> new IllegalArgumentException("신고자가 존재하지 않습니다."));

        if (reportRepository.existsByTargetTypeAndTargetIdAndReporterId("REVIEW", review.getId(), reporterId)) {
            throw new IllegalArgumentException("이미 신고한 리뷰입니다.");
        }

        Report report = Report.of(review, reporter, reason, detail);
        reportRepository.save(report);
    }
}
