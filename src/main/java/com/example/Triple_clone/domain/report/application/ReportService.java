package com.example.Triple_clone.domain.report.application;

import com.example.Triple_clone.common.logging.logMessage.MemberLogMessage;
import com.example.Triple_clone.common.logging.logMessage.ReportLogMessage;
import com.example.Triple_clone.common.logging.logMessage.ReviewLogMessage;
import com.example.Triple_clone.domain.member.domain.Member;
import com.example.Triple_clone.domain.report.domain.ReportTargetType;
import com.example.Triple_clone.domain.report.domain.Report;
import com.example.Triple_clone.domain.report.domain.ReportingReason;
import com.example.Triple_clone.domain.report.infra.ReportRepository;
import com.example.Triple_clone.domain.report.web.dto.ReportCreatedEvent;
import com.example.Triple_clone.domain.report.web.dto.ReportResponseDto;
import com.example.Triple_clone.domain.review.domain.Review;
import com.example.Triple_clone.domain.review.domain.ReviewStatus;
import com.example.Triple_clone.domain.member.infra.MemberRepository;
import com.example.Triple_clone.domain.review.infra.ReviewRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final MemberRepository memberRepository;
    private final ReviewRepository reviewRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public ReportResponseDto reportReview(Long reviewId, String email, ReportingReason reason, String detail) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> {
                    log.warn(ReviewLogMessage.REVIEW_SEARCH_FAILED.format(reviewId));
                    return new EntityNotFoundException("리뷰가 존재하지 않습니다.");
                });

        if (review.getStatus() == ReviewStatus.DELETED) {
            log.warn(ReviewLogMessage.REVIEW_SEARCH_FAILED.format(reviewId));
            throw new IllegalArgumentException("삭제된 리뷰는 신고할 수 없습니다.");
        }

        Member reporter = memberRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn(MemberLogMessage.MEMBER_SEARCH_FAILED_BY_EMAIL.format(email));
                    return new EntityNotFoundException("신고자가 존재하지 않습니다.");
                });

        if (reportRepository.existsByTargetTypeAndTargetIdAndReporterId(ReportTargetType.REVIEW, review.getId(), reporter.getId())) {
            log.warn(ReportLogMessage.DUPLICATED_REPORT.format(review.getClass().getSimpleName(), review.getId(), reporter.getId()));
            throw new IllegalArgumentException("이미 신고한 리뷰입니다.");
        }

        Report report = Report.of(review, reporter, reason, detail);
        reportRepository.save(report);

        long reportCount = reportRepository.countByTargetTypeAndTargetId(report.getTargetType(), report.getTargetId());

        eventPublisher.publishEvent(new ReportCreatedEvent(report, reportCount));
        return ReportResponseDto.from(report);
    }
}
