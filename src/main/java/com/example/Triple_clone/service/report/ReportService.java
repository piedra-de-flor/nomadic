package com.example.Triple_clone.service.report;

import com.example.Triple_clone.domain.entity.Member;
import com.example.Triple_clone.domain.entity.Report;
import com.example.Triple_clone.domain.entity.Review;
import com.example.Triple_clone.domain.vo.ReportTargetType;
import com.example.Triple_clone.domain.vo.ReportingReason;
import com.example.Triple_clone.domain.vo.ReviewStatus;
import com.example.Triple_clone.dto.report.ReportCreatedEvent;
import com.example.Triple_clone.dto.report.ReportResponseDto;
import com.example.Triple_clone.repository.MemberRepository;
import com.example.Triple_clone.repository.ReportRepository;
import com.example.Triple_clone.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final MemberRepository memberRepository;
    private final ReviewRepository reviewRepository;
    private final ApplicationEventPublisher eventPublisher;

    public ReportResponseDto reportReview(Long reviewId, String email, ReportingReason reason, String detail) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("리뷰가 존재하지 않습니다."));

        if (review.getStatus() == ReviewStatus.DELETED) {
            throw new IllegalArgumentException("삭제된 리뷰는 신고할 수 없습니다.");
        }

        Member reporter = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("신고자가 존재하지 않습니다."));

        if (reportRepository.existsByTargetTypeAndTargetIdAndReporterId(ReportTargetType.REVIEW, review.getId(), reporter.getId())) {
            throw new IllegalArgumentException("이미 신고한 리뷰입니다.");
        }

        Report report = Report.of(review, reporter, reason, detail);
        reportRepository.save(report);

        long reportCount = reportRepository.countByTargetTypeAndTargetId(report.getTargetType(), report.getTargetId());

        eventPublisher.publishEvent(new ReportCreatedEvent(report, reportCount));
        return ReportResponseDto.from(report);
    }
}
